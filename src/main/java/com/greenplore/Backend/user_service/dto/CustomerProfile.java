package com.greenplore.Backend.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerProfile implements Profile{
        String firstName ;
        String lastName ;
        String mobile ;
        String email ;
        String createdAt;

}
