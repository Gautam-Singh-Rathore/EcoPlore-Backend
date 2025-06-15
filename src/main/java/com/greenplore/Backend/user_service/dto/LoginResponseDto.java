package com.greenplore.Backend.user_service.dto;

public record LoginResponseDto(
        String accessToken ,
        String refreshToken
) {
}
