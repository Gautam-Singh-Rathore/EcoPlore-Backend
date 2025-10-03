package com.greenplore.Backend.order_service.dto;

import com.greenplore.Backend.product_service.dto.ProductCardResponseDto;
import com.greenplore.Backend.user_service.dto.AddressResponseDto;

public record OrderResponseDtoAdmin(
        Long id ,
        ProductCardResponseDto product,
        int quantity ,
        double amount ,
        AddressResponseDto deliveryAddress ,
        String awbNumber ,
        String courierName ,
        String shipmentLabelUrl,
        String shipmentStatus,
        Long shipmentId,
        String deliveryDate ,
        String sellerEmail
) {
}
