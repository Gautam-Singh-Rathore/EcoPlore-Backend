package com.greenplore.Backend.order_service.dto;

import java.util.UUID;

public record CartDto(
        UUID id ,
        int quantity
) {
}
