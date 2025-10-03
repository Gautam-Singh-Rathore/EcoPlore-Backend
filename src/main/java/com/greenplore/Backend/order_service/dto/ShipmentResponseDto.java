package com.greenplore.Backend.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShipmentResponseDto {
    private Boolean status;
    private ShipmentData data;

    @Data
    public static class ShipmentData {
        @JsonProperty("order_id")
        private Long orderId;

        @JsonProperty("shipment_id")
        private Long shipmentId;

        @JsonProperty("awb_number")
        private String awbNumber;

        @JsonProperty("courier_id")
        private String courierId;

        @JsonProperty("courier_name")
        private String courierName;

        @JsonProperty("status")
        private String status;

        @JsonProperty("additional_info")
        private String additionalInfo;

        @JsonProperty("payment_type")
        private String paymentType;

        @JsonProperty("label")
        private String label;

        @JsonProperty("manifest")
        private String manifest;
    }
}
