package com.greenplore.Backend.user_service.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class TestController {

    @GetMapping("public/hello")
    public String hello(){
        return "Hello and welcoms";
    }

    @GetMapping("private/greet")
    public String greet(){return "Authentication Object is :"+SecurityContextHolder.getContext().getAuthentication();}
    
}
