package com.greenplore.Backend.user_service.repo;

import com.greenplore.Backend.user_service.entity.Address;
import com.greenplore.Backend.user_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address,Long> {
    List<Address> findByCustomer(Customer customer);
}
