package com.greenplore.Backend.user_service.repo;

import com.greenplore.Backend.user_service.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepo extends JpaRepository<Admin , UUID> {
}
