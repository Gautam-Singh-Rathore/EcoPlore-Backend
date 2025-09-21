package com.greenplore.Backend.user_service.controller;

import com.greenplore.Backend.user_service.auth.JwtService;
import com.greenplore.Backend.user_service.auth.RefreshTokenService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.dto.*;
import com.greenplore.Backend.user_service.service.CustomerService;
import com.greenplore.Backend.user_service.service.SellerService;
import com.greenplore.Backend.user_service.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @PostMapping("/customer/signup")
    public ResponseEntity signUpCustomer(@RequestBody CustomerSignUpRequest request){
        return ResponseEntity.ok(customerService.registerCustomer(request));
    }

    @PostMapping("/seller/signup")
    public ResponseEntity signUpSeller(@RequestBody SellerSignUpRequest request){
        return ResponseEntity.ok(sellerService.registerSeller(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity verifyOTP(@RequestBody VerifyOTPRequest request) {
        String result = userService.verifyOTP(request.email(), request.otp());

        switch (result) {
            case "verified":
                return ResponseEntity.ok("Email verified successfully. You can now login.");
            case "expired":
                return ResponseEntity.badRequest().body("OTP has expired. Please request a new one.");
            case "invalid":
                return ResponseEntity.badRequest().body("Invalid OTP. Please try again.");
            case "no_otp_found":
                return ResponseEntity.badRequest().body("No OTP found. Please request a new one.");
            default:
                return ResponseEntity.badRequest().body("Verification failed.");
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity resendOTP(@RequestBody ResendOTPRequest request) {
        try {
            userService.generateAndSendOtp(request.email());
            return ResponseEntity.ok("OTP sent successfully to your email.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send OTP: " + e.getMessage());
        }
    }

    // Request OTP for password reset
    @PostMapping("/forgot-password/{email}")
    public ResponseEntity forgotPassword(
            @PathVariable String email
    ) {
        try {
            userService.generateAndSendOtp(email);
            return ResponseEntity.ok("OTP sent to your email for password reset.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Reset password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request.email(), request.otp(), request.newPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginRequestDto request , HttpServletResponse response){
        try {
            LoginResponseDto responseDto = userService.login(request);
            ResponseCookie accessCookie = ResponseCookie.from("accessToken" , responseDto.accessToken())
                    .httpOnly(true)
//                .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(60*60)
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken"  , responseDto.refreshToken())
                    .httpOnly(true)
//                .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(60*60*24*15)
                    .build();

            response.addHeader("Set-Cookie",accessCookie.toString());
            response.addHeader("Set-Cookie" , refreshCookie.toString());
            return ResponseEntity.ok("Login successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }



    
    
}
