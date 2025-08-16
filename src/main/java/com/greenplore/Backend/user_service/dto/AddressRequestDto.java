package com.greenplore.Backend.user_service.dto;

public record AddressRequestDto(
        String street ,
        String city ,
        String state ,
        String pinCode
) {
}
