package com.greenplore.Backend.order_service.entity;

import com.greenplore.Backend.product_service.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;
}
