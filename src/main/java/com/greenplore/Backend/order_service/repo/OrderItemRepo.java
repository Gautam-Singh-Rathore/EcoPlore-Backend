package com.greenplore.Backend.order_service.repo;

import com.greenplore.Backend.order_service.entity.Cart;
import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.order_service.entity.OrderItem;
import com.greenplore.Backend.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
    boolean existsByCartAndProduct(Cart cart , Product product);

    Optional<OrderItem> findByCartAndProduct(Cart custCart, Product product);
}
