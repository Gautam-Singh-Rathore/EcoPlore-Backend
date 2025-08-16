package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.product_service.exception.CustomerNotFound;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.dto.AddressRequestDto;
import com.greenplore.Backend.user_service.dto.AddressResponseDto;
import com.greenplore.Backend.user_service.entity.Address;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.AddressRepo;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private AddressRepo addressRepo ;

    public String addAddress(UserDetailsImpl user, AddressRequestDto addressRequestDto) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Address newAddress = Address.builder()
                .street(addressRequestDto.street())
                .city(addressRequestDto.city())
                .state(addressRequestDto.state())
                .pinCode(addressRequestDto.pinCode())
                .customer(customer)
                .build();

        addressRepo.save(newAddress);
        return "Address added";
    }


    public List<AddressResponseDto> getAddresses(UserDetailsImpl user) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));

        List<Address> addresses = addressRepo.findByCustomer(customer);
        return addresses.stream()
                .map(ad -> AddressResponseDto.from(ad))
                .collect(Collectors.toList());

    }
}
