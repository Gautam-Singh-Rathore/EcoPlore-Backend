package com.greenplore.Backend.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.dto.MeDetails;

@RestController
@RequestMapping("/api/v1/")
public class TestController {

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
            MeDetails userState = new MeDetails(user.getUsername(), authentication.getAuthorities().toString());
            return ResponseEntity.ok(userState);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("No existing user");
        }

    }
    
}
