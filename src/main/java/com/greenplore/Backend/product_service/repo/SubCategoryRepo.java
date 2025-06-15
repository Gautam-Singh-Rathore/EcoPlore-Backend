package com.greenplore.Backend.product_service.repo;

import com.greenplore.Backend.product_service.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepo extends JpaRepository<SubCategory , Long> {
}
