package com.greenplore.Backend.product_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryNotFound extends RuntimeException{
    private String msg;
}
