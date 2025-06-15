package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.user_service.auth.JwtService;
import com.greenplore.Backend.user_service.auth.RefreshTokenService;
import com.greenplore.Backend.user_service.dto.LoginRequestDto;
import com.greenplore.Backend.user_service.dto.LoginResponseDto;
import com.greenplore.Backend.user_service.entity.RefreshToken;
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
}
