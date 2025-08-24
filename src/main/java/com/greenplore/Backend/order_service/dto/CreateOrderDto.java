package com.greenplore.Backend.order_service.dto;

import com.greenplore.Backend.user_service.entity.Address;

import java.util.List;

public record CreateOrderDto(
    List<CartItemResponseDto> items ,
    Long addressId
) {
}
