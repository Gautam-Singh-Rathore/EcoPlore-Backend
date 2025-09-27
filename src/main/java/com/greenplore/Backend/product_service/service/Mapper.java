package com.greenplore.Backend.product_service.service;

import com.greenplore.Backend.product_service.dto.*;
import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.entity.SubCategory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class Mapper {

    public CategoryResponse categoryToCategoryResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public SubCategoryResponse subCategoryToSubCategoryResponse(SubCategory subCategory){
        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getImageUrl()
        );
    }

    public ProductCardResponseDto productsToProductsCardResponse(Product product){
        return new ProductCardResponseDto(
                product.getId(),
                product.getName(),
                product.getImageUrls().get(0),
                product.getPrice(),
                product.getNoOfUnits(),
                product.getDescription(),
                product.getSubCategory().getName()
        );
    }


    public ProductResponseDto productToProductResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDetails(),
                product.getNoOfUnits(),
                product.getImageUrls(),
                product.getSeller().getCompanyName(),
                product.getSubCategory().getId()
        );
    }

    public AddProductDto productToEditProductForm(Product product) {
        return new AddProductDto(
                product.getName(),
                product.getImageUrls(),
                product.getPrice(),
                product.getDescription(),
                product.getNoOfUnits(),
                product.getDetails(),
                product.getCategory().getId(),
                product.getSubCategory().getId(),
                product.getHeight(),
                product.getLength(),
                product.getWidth(),
                product.getWeight()
        );
    }
}
