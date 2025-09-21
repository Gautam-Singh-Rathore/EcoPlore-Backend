package com.greenplore.Backend.user_service.dto;

public record ResetPasswordRequest(String email, String otp, String newPassword) { }
