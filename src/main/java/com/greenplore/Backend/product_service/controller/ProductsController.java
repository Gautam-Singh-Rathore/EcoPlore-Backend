package com.greenplore.Backend.product_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/product")
public class ProductsController {

    @GetMapping("/category/{categoryId}")
    public ResponseEntity getProductsByCategory(@PathVariable Long categoryId){

    }

    @GetMapping("/sub_category/{subCategoryId}")
    public ResponseEntity getProductsBySubCategory(@PathVariable Long subCategoryId){

    }

    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable UUID
                                         id){

    }
}
