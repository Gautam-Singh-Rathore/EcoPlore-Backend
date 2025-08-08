package com.greenplore.Backend.order_service.entity;

import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.user_service.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name="wishlist_id"),
            inverseJoinColumns = @JoinColumn(name="product_id")
    )
    private List<Product> products ;
}
