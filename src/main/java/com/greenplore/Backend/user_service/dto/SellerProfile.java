package com.greenplore.Backend.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerProfile implements Profile {
    String email;
    String companyName;
    String mobile;
    String gst;
    String createdAt;
}

