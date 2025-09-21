package com.greenplore.Backend.user_service.entity;

import com.greenplore.Backend.user_service.entity.model.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true , nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // New field for email verification
    private Boolean isVerified = false;
    private String otp;
    private LocalDateTime otpExpiryTime;



    @PrePersist
    private void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    private void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens ;

    public User(String email, String password, Provider provider, String role) {
        this.email=email;
        this.password=password;
        this.provider=provider;
        this.role=role;
        this.isVerified = false;
    }


}
