package com.greenplore.Backend.product_service.entity;

import com.greenplore.Backend.user_service.entity.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private List<String> imageUrls;

    @Column(nullable = false)
    private Double price;

    @Column
    private Long noOfUnits;

    @Column(length = 1000 , nullable = false)
    private String description;

    @Lob
    private String details;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private Integer height;
    private Integer length;
    private Integer width;

    private Integer weight;

}
