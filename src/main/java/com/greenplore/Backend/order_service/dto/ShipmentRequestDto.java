package com.greenplore.Backend.order_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShipmentRequestDto {
    @JsonProperty("order_number")
    private String order_number;

    @JsonProperty("payment_type")
    private String payment_type;

    @JsonProperty("order_amount")
    private Double order_amount;

    @JsonProperty("package_weight")
    private Integer package_weight;

    @JsonProperty("package_length")
    private Integer package_length;

    @JsonProperty("package_breadth")
    private Integer package_breadth;

    @JsonProperty("package_height")
    private Integer package_height;

    private Consignee consignee;
    private Pickup pickup;

    @JsonProperty("order_items")
    private List<OrderItem> order_items;

    @Data
    @Builder
    public static class Consignee {
        private String name;
        private String address;

        @JsonProperty("address_2")
        private String address_2;

        private String city;
        private String state;
        private String pincode;
        private String phone;
    }

    @Data
    @Builder
    public static class Pickup {
        @JsonProperty("warehouse_name")
        private String warehouse_name;

        private String name;
        private String address;

        @JsonProperty("address_2")
        private String address_2;

        private String city;
        private String state;
        private String pincode;
        private String phone;
    }

    @Data
    @Builder
    public static class OrderItem {
        private String name;
        private String qty;
        private String price;
        private String sku;
    }
}
