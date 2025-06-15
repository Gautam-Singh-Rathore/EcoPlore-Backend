package com.greenplore.Backend.user_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserNotCreatedException extends RuntimeException{

    private String msg;
}
