package com.greenplore.Backend.user_service.repo;

import com.greenplore.Backend.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User , UUID> {

    Optional<User> findByEmail(String username);
}
