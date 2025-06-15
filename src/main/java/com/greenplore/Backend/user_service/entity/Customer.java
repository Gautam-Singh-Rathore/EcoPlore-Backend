package com.greenplore.Backend.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {

    @Id
    @GeneratedValue
    private UUID uid;

    @Column(nullable = false)
    private String firstName;
    private String lastName;

    @Column(nullable = false ,length = 10)
    private String mobileNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "customer",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<Address> addresses;


    public Customer(String firstName, String lastName, String mobile, User user) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.mobileNo=mobile;
        this.user=user;
    }
}
