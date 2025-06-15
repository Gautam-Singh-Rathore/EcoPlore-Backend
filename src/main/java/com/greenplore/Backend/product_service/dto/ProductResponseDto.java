package com.greenplore.Backend.product_service.dto;

import java.util.List;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name ,
        String description,
        Double price,
        String details,
        Long noOfUnits,
        List<String> images ,
        String sellerCompany ,
        Long subCategoryId
) {
}
