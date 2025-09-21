package com.greenplore.Backend.product_service.controller;

import com.greenplore.Backend.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/product")
public class ProductsControllerPublic {
    @Autowired
    private ProductService productService;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity getProductsByCategory(@PathVariable Long categoryId){
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/sub_category/{subCategoryId}")
    public ResponseEntity getProductsBySubCategory(@PathVariable Long subCategoryId){
        return ResponseEntity.ok(productService.getProductsBySubCategory(subCategoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity searchProducts(
            @PathVariable String name
    ){
        return ResponseEntity.ok(productService.searchProduct(name));
    }
}
