package com.greenplore.Backend.product_service.repo;

import com.greenplore.Backend.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category , Long> {

    List<Category> findAllByIsDeletedFalse();

    Optional<Category> findByIdAndIsDeletedFalse(Long aLong);

}
