package com.greenplore.Backend.user_service.handler;

import com.greenplore.Backend.user_service.exception.UserAlreadyPresentException;
import com.greenplore.Backend.user_service.exception.UserNotCreatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerHandler {

    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity handleUserExist(UserAlreadyPresentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists");
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity userNotCreated(UserNotCreatedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User Not Created ");
    }
}
