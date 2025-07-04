package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.user_service.dto.CustomerSignUpRequest;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.entity.model.Provider;
import com.greenplore.Backend.user_service.exception.UserAlreadyPresentException;
import com.greenplore.Backend.user_service.exception.UserNotCreatedException;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;

import java.net.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Boolean registerCustomer(CustomerSignUpRequest request) {
        if(userService.userExists(request.email())){
            throw  new UserAlreadyPresentException("User already present with email"+request.email());
        }
        try {
            User newUser = new User(
                    request.email(),
                    passwordEncoder.encode(request.password()),
                    Provider.LOCAL,
                    "CUSTOMER"
            );
            User savedUser = userRepo.save(newUser);
            Customer newCustomer = new Customer(
                    request.firstName(),
                    request.lastName(),
                    request.mobile(),
                    savedUser
            );
            Customer savedCustomer = customerRepo.save(newCustomer);
            return true;
        }catch (Exception e){
            throw new UserNotCreatedException("User not created error message : "+e.getMessage());
        }
    }
}
