package com.greenplore.Backend.product_service.controller;

import com.greenplore.Backend.product_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity getAllCategory(){
        return ResponseEntity.ok(categoryService.findAllCategory());
    }

    @GetMapping("/{id}/get")
    public ResponseEntity getSubCategoryByCategory(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoryService.findSubCategoryByCategoryId(id));
    }
}
