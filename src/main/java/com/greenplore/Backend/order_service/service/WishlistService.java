package com.greenplore.Backend.order_service.service;

import com.greenplore.Backend.order_service.entity.Wishlist;
import com.greenplore.Backend.order_service.repo.WishlistRepo;
import com.greenplore.Backend.product_service.dto.ProductCardResponseDto;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.exception.CustomerNotFound;
import com.greenplore.Backend.product_service.exception.ProductNotFoundException;
import com.greenplore.Backend.product_service.repo.ProductRepo;
import com.greenplore.Backend.product_service.service.Mapper;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WishlistService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private ProductRepo productRepo;
    private WishlistRepo wishlistRepo;
    private Mapper mapper;

    @Transactional
    public String addToWishlist(UserDetailsImpl user, UUID id) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));
        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Product product = productRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        Wishlist wishlist = wishlistRepo.findByCustomer(customer);
        wishlist.getProducts().add(product);
        wishlistRepo.save(wishlist);
        return "Product added to wishlist";
    }

    public boolean isPresentInUserWishlist(UserDetailsImpl user , UUID id){
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));
        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Product product = productRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        Wishlist wishlist = wishlistRepo.findByCustomer(customer);
        if(wishlist.getProducts().contains(product)) return true;
        return false;
    }

    public String removeFromWishlist(UserDetailsImpl user , UUID id){
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));
        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Product product = productRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        Wishlist wishlist = wishlistRepo.findByCustomer(customer);
        wishlist.getProducts().remove(product);
        wishlistRepo.save(wishlist);
        return "Product removed from wishlist";
    }

    public List<ProductCardResponseDto> getUserWishlist(UserDetailsImpl user){
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));
        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));

        Wishlist wishlist = wishlistRepo.findByCustomer(customer);

        List<ProductCardResponseDto> list = wishlist.getProducts()
                .stream()
                .filter((var product )-> product.isDeleted()==false)
                .map(mapper::productsToProductsCardResponse)
                .collect(Collectors.toList());
        return list;
    }
}
