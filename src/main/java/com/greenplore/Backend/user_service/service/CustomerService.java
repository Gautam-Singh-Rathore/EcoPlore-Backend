package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.order_service.entity.Cart;
import com.greenplore.Backend.order_service.entity.Wishlist;
import com.greenplore.Backend.order_service.repo.CartRepo;
import com.greenplore.Backend.order_service.repo.WishlistRepo;
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
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private WishlistRepo wishlistRepo;

    @Transactional
    public String registerCustomer(CustomerSignUpRequest request) {
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
            Cart cart = Cart.builder()
                    .customer(savedCustomer)
                    .build();
            cartRepo.save(cart);

            Wishlist wishlist = Wishlist.builder()
                    .customer(savedCustomer)
                    .build();
            wishlistRepo.save(wishlist);
            // Generate and send OTP for email verification
            userService.generateAndSendOtp(request.email());
            return "Customer registered successfully. Please check your email for OTP verification.";
        }catch (Exception e){
            throw new UserNotCreatedException("User not created error message : "+e.getMessage());
        }
    }
}
