package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.user_service.dto.SellerSignUpRequest;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.entity.model.PickUpAddress;
import com.greenplore.Backend.user_service.entity.model.Provider;
import com.greenplore.Backend.user_service.entity.model.SellerBankDetails;
import com.greenplore.Backend.user_service.exception.UserAlreadyPresentException;
import com.greenplore.Backend.user_service.exception.UserNotCreatedException;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public Boolean registerSeller(SellerSignUpRequest request) {
        if(userService.userExists(request.email())){
            throw  new UserAlreadyPresentException("User already present with email"+request.email());
        }
        try {
            User newUser = new User(
                    request.email(),
                    passwordEncoder.encode(request.password()),
                    Provider.LOCAL,
                    "SELLER"
            );
            User savedUser = userRepo.save(newUser);
            Seller newSeller = Seller.builder()
                .companyName(request.companyName())
                .GSTNumber(request.GSTNumber())
                .mobileNo(request.mobileNo())
                .pickUpAddress(new PickUpAddress(
                        request.buildingNo(),
                        request.street(),
                        request.landmark(),
                        request.pinCode(),
                        request.city(),
                        request.state())
                )
                .bankDetails(new SellerBankDetails(
                        request.fullName(),
                        request.accountNo(),
                        request.IFSCCode()
                ))
                    .user(savedUser)
                .build();
            Seller  savedSeller = sellerRepo.save(newSeller);
            return true;
        }catch (Exception e){
            throw new UserNotCreatedException("User not created error message : "+e.getMessage());
        }
    }
}
