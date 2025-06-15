package com.greenplore.Backend.user_service.controller;

import com.greenplore.Backend.user_service.auth.JwtService;
import com.greenplore.Backend.user_service.auth.RefreshTokenService;
import com.greenplore.Backend.user_service.dto.CustomerSignUpRequest;
import com.greenplore.Backend.user_service.dto.LoginRequestDto;
import com.greenplore.Backend.user_service.dto.LoginResponseDto;
import com.greenplore.Backend.user_service.dto.SellerSignUpRequest;
import com.greenplore.Backend.user_service.service.CustomerService;
import com.greenplore.Backend.user_service.service.SellerService;
import com.greenplore.Backend.user_service.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginRequestDto request , HttpServletResponse response){

        LoginResponseDto responseDto = userService.login(request);
        ResponseCookie accessCookie = ResponseCookie.from("accessToken" , responseDto.accessToken())
                .httpOnly(true)
//                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60*60)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken"  , responseDto.refreshToken())
                .httpOnly(true)
//                .secure(true)
                .sameSite("Strick")
                .path("/")
                .maxAge(60*60*24*15)
                .build();

        response.addHeader("Set-Cookie",accessCookie.toString());
        response.addHeader("Set-Cookie" , refreshCookie.toString());

        return ResponseEntity.ok("Login successfully");
    }
}
