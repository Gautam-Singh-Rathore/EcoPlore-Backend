package com.greenplore.Backend.order_service.dto;

import com.greenplore.Backend.product_service.dto.ProductCardResponseDto;
import com.greenplore.Backend.user_service.dto.AddressResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public record OrderResponseDto(
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
        String deliveryDate
) {
}
