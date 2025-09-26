package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.product_service.exception.SellerNotFound;
import com.greenplore.Backend.user_service.auth.JwtService;
import com.greenplore.Backend.user_service.auth.RefreshTokenService;
import com.greenplore.Backend.user_service.dto.*;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.RefreshToken;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.EmailNotVerifiedException;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import com.greenplore.Backend.user_service.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpUtil otpUtil;

    public Boolean userExists(String email){
        return userRepo.findByEmail(email).isPresent();
    }

    // Otp and verify methods
    public void generateAndSendOtp(String email){
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

        String otp = otpUtil.generateSixDigitOTP();
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10)); // 5 minutes validity

        userRepo.save(user);
        emailService.sendOTPEmail(email, otp);
    }

    public String verifyOTP(String email, String providedOtp) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

        if (user.getOtp() == null || user.getOtpExpiryTime() == null) {
            return "no_otp_found";
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
            // Clear expired OTP
            user.setOtp(null);
            user.setOtpExpiryTime(null);
            userRepo.save(user);
            return "expired";
        }

        if (!user.getOtp().equals(providedOtp)) {
            return "invalid";
        }

        // OTP is valid, verify the user
        user.setIsVerified(true);
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepo.save(user);

        return "verified";
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user is verified
        if (!user.getIsVerified()) {
            throw new EmailNotVerifiedException("Email not verified. Please verify your email first.");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email() , request.password()));
        if(authentication.isAuthenticated()){
            return new LoginResponseDto(
                    jwtService.generateToken(request.email()),
                    refreshTokenService.createRefreshToken(request.email()).getToken()
            );
        }else {
            throw new BadCredentialsException("Bad Credentials");
        }
    }


    public Profile getProfile(String username) {
        User user = userRepo.findByEmail(username)
                .orElseThrow(()-> {
                    System.out.println("No user found for user: ");
                    return new UserNotFoundException("User not found for email :" + username);
                });
        System.out.println(user.getRole());
        if ("SELLER".equals(user.getRole())){
            Seller seller = sellerRepo.findByUserId(user.getId())
                    .orElseThrow(()-> {
                        System.out.println("No seller found for user: ");
                        return new SellerNotFound("Seller not found for email :" + username);
                    });

            return new SellerProfile(
                    username ,
                    seller.getCompanyName(),
                    seller.getMobileNo(),
                    seller.getGSTNumber(),
                    user.getCreatedAt().toString()
            );
        }else if ("CUSTOMER".equals(user.getRole())){
            Customer cust = customerRepo.findByUser(user)
                    .orElseThrow(()-> new SellerNotFound("Seller not found for email :"+username));
            return new CustomerProfile(
                    cust.getFirstName(),
                    cust.getLastName(),
                    cust.getMobileNo(),
                    username,
                    user.getCreatedAt().toString()
            );
        }else {
            throw new RuntimeException("User role is not recognized: " + user.getRole());
        }

    }

    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

        // Check OTP validity
        if (user.getOtp() == null || user.getOtpExpiryTime() == null) {
            throw new RuntimeException("No OTP found. Please request a new one.");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
            user.setOtp(null);
            user.setOtpExpiryTime(null);
            userRepo.save(user);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (!user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP. Please try again.");
        }

        // OTP is valid, reset the password (make sure password is hashed)
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiryTime(null);

        userRepo.save(user);
    }

}
