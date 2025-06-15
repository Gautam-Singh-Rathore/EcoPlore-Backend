package com.greenplore.Backend.user_service.repo;

import com.greenplore.Backend.user_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken , Integer> {

    Optional<RefreshToken> findByToken(String token);
}
