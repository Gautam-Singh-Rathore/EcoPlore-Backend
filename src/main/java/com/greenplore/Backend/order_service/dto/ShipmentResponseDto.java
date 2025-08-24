package com.greenplore.Backend.order_service.dto;

import lombok.Data;

@Data
public class ShipmentResponseDto {
    private Boolean status;
    private ShipmentData data;

    @Data
    public static class ShipmentData {
        private Long orderId;
        private Long shipmentId;
        private String awbNumber;
        private String courierId;
        private String courierName;
        private String status;
        private String additionalInfo;
        private String paymentType;
        private String label;
        private String manifest;
    }
}
