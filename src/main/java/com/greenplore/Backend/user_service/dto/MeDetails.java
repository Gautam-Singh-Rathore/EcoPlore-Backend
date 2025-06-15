package com.greenplore.Backend.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record MeDetails(
        String email ,
        String role
) {
}
