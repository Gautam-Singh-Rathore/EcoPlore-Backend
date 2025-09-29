package com.greenplore.Backend.product_service.dto;

import java.util.UUID;

public record ProductCardResponseDto(
        UUID id ,
        String name,
        String imageUrl ,
        Double price,
        Long noOfUnits,
        String description,
        String subCategoryName
) {

}
