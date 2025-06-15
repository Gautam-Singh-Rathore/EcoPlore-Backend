package com.greenplore.Backend.user_service.dto;

import jakarta.persistence.Column;

public record SellerSignUpRequest(
        String email ,
        String password ,
        String companyName ,
        String GSTNumber ,
        String mobileNo ,
        String buildingNo,
        String street,
        String landmark,
        String pinCode,
        String city,
        String state,
        String fullName ,
        String accountNo ,
        String IFSCCode

) {
}
