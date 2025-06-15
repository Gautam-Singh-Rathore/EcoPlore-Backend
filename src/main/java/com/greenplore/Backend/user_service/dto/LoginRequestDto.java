package com.greenplore.Backend.user_service.dto;

public record LoginRequestDto(
        String email,
        String password
) {
}
