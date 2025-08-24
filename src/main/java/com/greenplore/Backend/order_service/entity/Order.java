package com.greenplore.Backend.order_service.entity;

import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.user_service.entity.Address;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller ;


    private Double orderAmount;

    // Address
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    // Shipment details
    @Column(name = "awb_number")
    private String awbNumber;

    @Column(name = "courier_name")
    private String courierName;

    @Column(name = "shipment_label_url")
    private String shipmentLabelUrl;

    @Column(name = "shipment_status")
    private String shipmentStatus;

    @Column(name = "shipment_id")
    private Long shipmentId;
}
