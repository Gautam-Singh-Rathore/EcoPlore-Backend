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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private AddressRepo addressRepo ;

    @Transactional
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
                .isDeleted(false)
                .isDefault(true)
                .build();

        for(Address ad : customer.getAddresses()){
            ad.setDefault(false);
            addressRepo.save(ad);
        }

        addressRepo.save(newAddress);
        return "Address added";
    }


    public List<AddressResponseDto> getAddresses(UserDetailsImpl user) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));

        List<Address> addresses = addressRepo.findByCustomerAndIsDeletedFalse(customer);
        return addresses.stream()
                .map(ad -> AddressResponseDto.from(ad))
                .collect(Collectors.toList());

    }

    public String deleteAddress(UserDetailsImpl user, Long id) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Address address = addressRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Address not available"));

        address.setDeleted(true);
        addressRepo.save(address);
        return "Address deleted";

    }

    public String editAddress(UserDetailsImpl user, AddressRequestDto addressRequestDto, Long id) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Address address = addressRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Address not available"));

        address.setStreet(addressRequestDto.street());
        address.setCity(addressRequestDto.city());
        address.setState(addressRequestDto.state());
        address.setPinCode(addressRequestDto.pinCode());

        addressRepo.save(address);
        return "Address edited";
    }

    @Transactional
    public String makeAddressDefault(UserDetailsImpl user, Long id) {

        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Address address = addressRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Address not available"));

        address.setDefault(true);
        for(Address ad : customer.getAddresses()){
            ad.setDefault(false);
            addressRepo.save(ad);
        }
        return "Address set as default";
    }
}
