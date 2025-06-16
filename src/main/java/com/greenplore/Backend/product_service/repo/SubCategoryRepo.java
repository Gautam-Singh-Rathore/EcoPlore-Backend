package com.greenplore.Backend.product_service.repo;

import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepo extends JpaRepository<SubCategory , Long> {

    List<SubCategory> findByCategory(Category category);
}
