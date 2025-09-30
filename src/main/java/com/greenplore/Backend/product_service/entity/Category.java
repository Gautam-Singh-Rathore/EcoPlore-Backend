package com.greenplore.Backend.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SubCategory> subCategories;

    @OneToMany(mappedBy = "category" ,  cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Product> products;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
