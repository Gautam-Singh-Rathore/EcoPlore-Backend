package com.greenplore.Backend.user_service.entity;

import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.user_service.entity.model.PickUpAddress;
import com.greenplore.Backend.user_service.entity.model.SellerBankDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Seller {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue
    private UUID uid;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 15)
    private String GSTNumber;

    @Column(nullable = false , length = 10)
    private String mobileNo;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @Embedded
    private PickUpAddress pickUpAddress;

    @Embedded
    private SellerBankDetails bankDetails;

    @OneToMany(mappedBy = "seller" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Product> products;


}
