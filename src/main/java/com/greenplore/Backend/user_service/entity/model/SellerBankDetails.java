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
public class SellerBankDetails {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String accountNo;
    @Column(nullable = false)
    private String IFSCCode;
}
