package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.product_service.exception.SellerNotFound;
import com.greenplore.Backend.user_service.auth.JwtService;
import com.greenplore.Backend.user_service.auth.RefreshTokenService;
import com.greenplore.Backend.user_service.dto.*;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.RefreshToken;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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

    public Boolean userExists(String email){
        return userRepo.findByEmail(email).isPresent();
    }

    public LoginResponseDto login(LoginRequestDto request) {
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

}
