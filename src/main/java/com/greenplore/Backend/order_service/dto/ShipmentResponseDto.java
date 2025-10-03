package com.greenplore.Backend.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShipmentResponseDto {
    private Boolean status;
    private ShipmentData data;

    @Data
    public static class ShipmentData {
        private Long order_id;

        private Long shipment_id;

        private String awb_number;

        private String courier_id;

        private String courier_name;

        private String status;

        private String additional_info;

        private String payment_type;

        private String label;

        private String manifest;
    }
}
