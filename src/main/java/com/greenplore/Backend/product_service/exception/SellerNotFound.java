package com.greenplore.Backend.product_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerNotFound extends RuntimeException{
    private String msg;
}
