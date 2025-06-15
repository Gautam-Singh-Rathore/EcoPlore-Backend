package com.greenplore.Backend.product_service.service;

import com.greenplore.Backend.product_service.dto.ProductCardResponseDto;
import com.greenplore.Backend.product_service.dto.ProductResponseDto;
import com.greenplore.Backend.product_service.entity.Product;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class Mapper {

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
}
