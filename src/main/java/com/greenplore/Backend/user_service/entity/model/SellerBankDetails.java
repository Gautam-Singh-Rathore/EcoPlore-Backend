package com.greenplore.Backend.user_service.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerBankDetails {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String accountNo;
    @Column(nullable = false)
    private String IFSCCode;
}
