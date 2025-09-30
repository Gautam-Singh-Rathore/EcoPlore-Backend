package com.greenplore.Backend.product_service.service;

import com.greenplore.Backend.product_service.dto.CategoryResponse;
import com.greenplore.Backend.product_service.dto.SubCategoryResponse;
import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.SubCategory;
import com.greenplore.Backend.product_service.exception.CategoryNotFound;
import com.greenplore.Backend.product_service.repo.CategoryRepo;
import com.greenplore.Backend.product_service.repo.SubCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private Mapper mapper;
    @Autowired
    private SubCategoryRepo subCategoryRepo;


    public List<CategoryResponse> findAllCategory() {
        List<Category> categories = categoryRepo.findAllByIsDeletedFalse();
        return categories.stream()
                .map(mapper::categoryToCategoryResponse)
                .toList();

    }

    public List<SubCategoryResponse> findSubCategoryByCategoryId(Long id) {
        List<SubCategory> subCategories = subCategoryRepo.findAllByIsDeletedFalse();
        return subCategories.stream()
                .filter((var sub)-> sub.getCategory().getId()==id )
                .map(mapper::subCategoryToSubCategoryResponse)
                .toList();
//        Category category = categoryRepo.findById(id)
//                .orElseThrow(()-> new CategoryNotFound("Category not found for id: "+id
//                ));
//        List<SubCategory> subCategories = subCategoryRepo.findByCategory(category);
//        return subCategories.stream()
//                .map(mapper::subCategoryToSubCategoryResponse)
//                .toList();
    }

    public List<SubCategoryResponse> findAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepo.findAllByIsDeletedFalse();
        return subCategories.stream()
                .map(mapper::subCategoryToSubCategoryResponse)
                .toList();
    }
}
