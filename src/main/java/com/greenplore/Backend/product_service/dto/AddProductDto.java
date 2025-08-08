package com.greenplore.Backend.product_service.dto;

import java.util.List;

public record AddProductDto(
        String name,
        List<String> imageUrls,
        Double price,
        String description,
        Long noOfUnits,
        String details,
        Long categoryId,
        Long subCategoryId ,
        Double height ,
        Double length ,
        Double width ,
        Double weight
        ) {
}
