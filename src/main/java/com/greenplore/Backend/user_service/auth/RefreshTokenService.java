package com.greenplore.Backend.user_service.auth;

import com.greenplore.Backend.user_service.entity.RefreshToken;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.repo.RefreshTokenRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepo userRepo;

    private static final Integer RefreshTokenExpiryDays = 15;

    public RefreshToken createRefreshToken(String username){
        User user = userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for email "+ username));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(RefreshTokenExpiryDays, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepo.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken isRefreshTokenExpired(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
