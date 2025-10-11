package com.greenplore.Backend.product_service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenplore.Backend.user_service.entity.Seller;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String imageUrlsSerialized;

    @Column(nullable = false)
    private Double price;

    @Column
    private Long noOfUnits;

    @Column(length = 1000 , nullable = false)
    private String description;

    @Lob
    private String details;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private Integer height;
    private Integer length;
    private Integer width;

    private Integer weight;

    // Method to serialize List<String> to a String before storing it in the database
    public void setImageUrls(List<String> imageUrls) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.imageUrlsSerialized = objectMapper.writeValueAsString(imageUrls);  // Convert List to String (JSON)
    }

    // Method to deserialize the String back into a List<String> when retrieving the data
    public List<String> getImageUrls() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.imageUrlsSerialized, List.class);  // Convert String back to List
    }

}
