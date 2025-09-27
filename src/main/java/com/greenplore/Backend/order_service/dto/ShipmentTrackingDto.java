package com.greenplore.Backend.order_service.dto;

public class ShipmentTrackingDto {
    private String edd;
    private String status;

    public ShipmentTrackingDto() {}

    public ShipmentTrackingDto(String edd, String status) {
        this.edd = edd;
        this.status = status;
    }

    public String getEdd() {
        return edd;
    }

    public void setEdd(String edd) {
        this.edd = edd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
