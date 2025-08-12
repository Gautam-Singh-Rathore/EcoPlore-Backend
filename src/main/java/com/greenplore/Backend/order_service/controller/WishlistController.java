package com.greenplore.Backend.order_service.controller;

import com.greenplore.Backend.order_service.service.WishlistService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/wishlist")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/get")
    public ResponseEntity<?> getMyWishList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(wishlistService.getUserWishlist(user));
    }

    @GetMapping("/add/{id}")
    public ResponseEntity<?> addToWishList(
            @PathVariable UUID id
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(wishlistService.addToWishlist(user , id));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable UUID id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(wishlistService.removeFromWishlist(user , id));
    }

    @GetMapping("/product-exists/{id}")
    public ResponseEntity<?> isProductPresentInUserWishlist(
            @PathVariable UUID id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(wishlistService.isPresentInUserWishlist(user , id));
    }
}
