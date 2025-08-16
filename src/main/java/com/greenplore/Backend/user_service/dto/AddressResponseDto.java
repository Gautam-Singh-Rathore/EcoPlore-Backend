package com.greenplore.Backend.user_service.dto;

import com.greenplore.Backend.user_service.entity.Address;

public record AddressResponseDto(
        Long id ,
        String street ,
        String city ,
        String state ,
        String pinCode
) {
    public static AddressResponseDto from(Address address){
        return new AddressResponseDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPinCode()
        );
    }
}
