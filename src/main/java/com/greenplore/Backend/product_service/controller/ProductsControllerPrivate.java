package com.greenplore.Backend.product_service.controller;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.greenplore.Backend.product_service.dto.AddProductDto;
import com.greenplore.Backend.product_service.service.ProductService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/product")
public class ProductsControllerPrivate {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity addProducts(
             @RequestBody AddProductDto productDto
            ){
        System.out.println(productDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(productService.addProduct(user , productDto));
    }
    // edit product
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity editProduct(
            @PathVariable UUID id,
            @RequestBody AddProductDto product
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.editProduct(user , id , product));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong please try again after some time");
        }

    }

    //get edit product form
    @GetMapping("/edit/get-form/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity getEditProductForm(@PathVariable UUID id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.getEditProductForm(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong please try again after some time");
        }
    }

    // delete product
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity deleteProduct(@PathVariable UUID id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(user , id ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong please try again after some time");
        }
    }
}
