package com.greenplore.Backend.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin{
    @Id
    @GeneratedValue
    private UUID uid;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false , length = 10)
    private String mobileNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
