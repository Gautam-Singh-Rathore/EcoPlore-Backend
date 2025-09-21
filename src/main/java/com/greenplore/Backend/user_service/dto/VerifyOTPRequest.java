package com.greenplore.Backend.user_service.dto;

public record VerifyOTPRequest(String email, String otp) {}
