package com.greenplore.Backend.user_service.repo;

import com.greenplore.Backend.user_service.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepo extends JpaRepository<Seller , UUID> {
}
