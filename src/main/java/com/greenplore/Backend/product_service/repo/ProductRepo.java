package com.greenplore.Backend.product_service.repo;

import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.entity.SubCategory;
import com.greenplore.Backend.user_service.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepo extends JpaRepository<Product , UUID> {
    
    Optional<List<Product>> findByCategory(Category category);

    Optional<List<Product>> findBySubCategory(SubCategory subCategory);

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String name1);

    List<Product> findBySeller(Seller seller);

}
