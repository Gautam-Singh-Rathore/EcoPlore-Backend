package com.greenplore.Backend.order_service.repo;

import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
}
