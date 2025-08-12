package com.greenplore.Backend.order_service.repo;

import com.greenplore.Backend.order_service.entity.Wishlist;
import com.greenplore.Backend.user_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist,Long> {
    Wishlist findByCustomer(Customer customer);
}
