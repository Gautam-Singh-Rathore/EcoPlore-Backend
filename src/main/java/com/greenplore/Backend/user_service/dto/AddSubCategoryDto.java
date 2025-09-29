package com.greenplore.Backend.user_service.dto;

public record AddSubCategoryDto(
        String name ,
        String imageUrl,
        Long categoryId
) {
}
