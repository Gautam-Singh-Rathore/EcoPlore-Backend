package com.greenplore.Backend.order_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/wishlist")
public class WishlistController {

//    @GetMapping("/get")
//    public ResponseEntity<?> getMyWishList(){
//        // Logic to get all wishlist products of a user
//    }
//
//    @GetMapping("/add/{id}")
//    public ResponseEntity<?> addToWishList(
//            @PathVariable UUID id
//            ){
//        //Logic to add to wishlist
//    }
//
//    @GetMapping("/remove/{id}")
//    public ResponseEntity<?> removeFromWishlist(
//            @PathVariable UUID id
//    ){
//        // Logic to remove from wishlist -> if exist in wishlist or not
//    }
}
