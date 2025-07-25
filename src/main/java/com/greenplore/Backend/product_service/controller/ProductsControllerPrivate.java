package com.greenplore.Backend.product_service.controller;
import com.greenplore.Backend.product_service.dto.AddProductDto;
import com.greenplore.Backend.product_service.service.ProductService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
