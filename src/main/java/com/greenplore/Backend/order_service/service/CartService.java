package com.greenplore.Backend.order_service.service;

import com.greenplore.Backend.order_service.dto.CartDto;
import com.greenplore.Backend.order_service.dto.CartItemResponseDto;
import com.greenplore.Backend.order_service.entity.Cart;
import com.greenplore.Backend.order_service.entity.OrderItem;
import com.greenplore.Backend.order_service.repo.CartRepo;
import com.greenplore.Backend.order_service.repo.OrderItemRepo;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.exception.CustomerNotFound;
import com.greenplore.Backend.product_service.exception.ProductNotFoundException;
import com.greenplore.Backend.product_service.repo.ProductRepo;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepo cartRepo;
    private OrderItemRepo orderItemRepo;
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private ProductRepo productRepo;

    @Transactional
    public String addToCart(UserDetailsImpl user, CartDto item) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));

        Cart custCart = cartRepo.findByCustomer(customer);

        Product product = productRepo.findById(item.id())
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .cart(custCart)
                .quantity(item.quantity())
                .build();

         orderItemRepo.save(orderItem);
         return "Product added to cart";

    }
    public boolean isProductInCustomersCart(UserDetailsImpl user , UUID productId){
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Cart custCart = cartRepo.findByCustomer(customer);
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
        return orderItemRepo.existsByCartAndProduct(custCart,product);
    }

    @Transactional
    public String editProductInCart(UserDetailsImpl user, CartDto item) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Cart custCart = cartRepo.findByCustomer(customer);
        Product product = productRepo.findById(item.id())
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        OrderItem orderItem = orderItemRepo.findByCartAndProduct(custCart, product)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        orderItem.setQuantity(item.quantity());
        orderItemRepo.save(orderItem);
        return "Product quantity updated in cart";
    }

    public List<CartItemResponseDto> getUserCart(UserDetailsImpl user) {
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));

        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Cart custCart = cartRepo.findByCustomer(customer);

        List<OrderItem> orderItems = custCart.getItems();
        List<CartItemResponseDto> responseDtos = orderItems.stream()
                .map(orderItem -> CartItemResponseDto.from(orderItem.getId(),orderItem.getProduct() , orderItem.getQuantity()))
                .collect(Collectors.toList());
        return responseDtos;
    }
}
