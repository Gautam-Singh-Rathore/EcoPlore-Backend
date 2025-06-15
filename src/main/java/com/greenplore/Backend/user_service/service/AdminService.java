package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.user_service.repo.AdminRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AdminRepo adminRepo;
}
