package com.greenplore.Backend.product_service.repo;

import com.greenplore.Backend.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category , Long> {
}
