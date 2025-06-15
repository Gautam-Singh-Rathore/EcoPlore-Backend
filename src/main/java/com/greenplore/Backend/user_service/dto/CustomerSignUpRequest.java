package com.greenplore.Backend.user_service.dto;

public record CustomerSignUpRequest(
        String email ,
        String password ,
        String firstName ,
        String lastName ,
        String mobile
) {
}
