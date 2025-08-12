package com.greenplore.Backend.order_service.dto;

import com.greenplore.Backend.product_service.entity.Product;

import java.util.List;
import java.util.UUID;

public record CartItemResponseDto(
        Long cartItemId,
        UUID productId,
        String productName,
        String imageUrls,
        Double price,
        int quantity
) {
    public static CartItemResponseDto from(Long cartItemId,Product product,int quantity){
        return new CartItemResponseDto(
                cartItemId,
                product.getId(),
                product.getName(),
                product.getImageUrls().getFirst(),
                product.getPrice(),
                quantity
        );
    }
}
