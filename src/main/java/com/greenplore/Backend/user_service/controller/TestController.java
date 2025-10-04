package com.greenplore.Backend.user_service.controller;

import com.greenplore.Backend.user_service.dto.*;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.service.AddressService;
import com.greenplore.Backend.user_service.service.CustomerService;
import com.greenplore.Backend.user_service.service.SellerService;
import com.greenplore.Backend.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.greenplore.Backend.user_service.auth.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1/")
public class TestController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SellerService sellerService;

    @GetMapping("public/hello")
    public String hello(){
        return "Hello and welcoms";
    }

    @GetMapping("private/greet")
    public String greet(){return "Authentication Object is :"+SecurityContextHolder.getContext().getAuthentication();}

    @GetMapping("private/me")
    public ResponseEntity getUserState(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            MeDetails userState = new MeDetails(user.getUsername(), authentication.getAuthorities().stream().toList().get(0).toString());
            return ResponseEntity.ok(userState);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No existing user");
        }

    }

    @GetMapping("private/address/get")
    public ResponseEntity<?> getUserAddresses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(addressService.getAddresses(user));
    }

    @PostMapping("/private/address/add")
    public ResponseEntity<?> addUserAddress(
            @RequestBody AddressRequestDto addressRequestDto
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(addressService.addAddress(user , addressRequestDto));
    }

    @PostMapping("/private/address/delete/{id}")
    public ResponseEntity deleteAddress(
            @PathVariable Long id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(addressService.deleteAddress(user , id));
    }

    @PostMapping("/private/address/make-default/{id}")
    public ResponseEntity makeDefaultAddress(
            @PathVariable Long id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(addressService.makeAddressDefault(user , id));
    }

    @PostMapping("/private/address/edit/{id}")
    public ResponseEntity editAddress(
            @PathVariable Long id ,
            @RequestBody AddressRequestDto addressRequestDto
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(addressService.editAddress(user, addressRequestDto , id));
    }

    // Get profile (same for seller and buyer)

    @GetMapping("private/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            Profile profile = userService.getProfile(user.getUsername());
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            }
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    // Edit profile for customer
    @PostMapping("private/profile/edit-customer")
    public ResponseEntity editCustomerProfile(
            @RequestBody CustomerProfile profile
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.editCustomer(user , profile));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong please try again after some time");
        }
    }


    @PostMapping("private/profile/edit-seller")
    public ResponseEntity editSellerProfile(
            @RequestBody SellerProfile profile
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(sellerService.editSeller(user , profile));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong please try again after some time");
        }
    }



}
