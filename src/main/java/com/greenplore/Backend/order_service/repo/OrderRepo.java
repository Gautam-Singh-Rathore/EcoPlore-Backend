package com.greenplore.Backend.order_service.repo;

import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.order_service.entity.OrderItem;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    
    List<Order> findBySeller(Seller seller);

    List<Order> findByCustomer(Customer customer);
}
