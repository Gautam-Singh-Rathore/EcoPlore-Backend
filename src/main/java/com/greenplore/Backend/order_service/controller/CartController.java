package com.greenplore.Backend.order_service.controller;

import com.greenplore.Backend.order_service.dto.CartDto;
import com.greenplore.Backend.order_service.service.CartService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.getUserCart(user));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody CartDto item
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.addToCart(user , item));
    }

    @GetMapping("/product-exists/{id}")
    public ResponseEntity<?> doesProductExistsInCart(
            @PathVariable UUID id
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.isProductInCustomersCart(user,id));
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editProductInCart(
            @RequestBody CartDto item
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        String response = cartService.editProductInCart(user, item);
        return ResponseEntity.ok(response);
    }
}
