package com.greenplore.Backend.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Address{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    @Column(length = 6)
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
