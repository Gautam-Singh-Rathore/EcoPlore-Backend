package com.greenplore.Backend.user_service.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PickUpAddress {
    @Column(nullable = false)
    private String buildingNo;
    @Column(nullable = false)
    private String street;
    private String landmark;
    @Column(nullable = false)
    private String pinCode;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
}
