package com.greenplore.Backend.product_service.service;

import com.greenplore.Backend.order_service.dto.OrderResponseDto;
import com.greenplore.Backend.order_service.dto.ShipmentTrackingDto;
import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.order_service.service.ShipmentService;
import com.greenplore.Backend.product_service.dto.*;
import com.greenplore.Backend.product_service.entity.Category;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.entity.SubCategory;
import com.greenplore.Backend.user_service.dto.AddressResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class Mapper {
    @Autowired
    private ShipmentService shipmentService;

    public CategoryResponse categoryToCategoryResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public SubCategoryResponse subCategoryToSubCategoryResponse(SubCategory subCategory){
        return new SubCategoryResponse(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getImageUrl()
        );
    }

    public ProductCardResponseDto productsToProductsCardResponse(Product product){
        return new ProductCardResponseDto(
                product.getId(),
                product.getName(),
                product.getImageUrls().get(0),
                product.getPrice(),
                product.getNoOfUnits(),
                product.getDescription(),
                product.getSubCategory().getName()
        );
    }


    public ProductResponseDto productToProductResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDetails(),
                product.getNoOfUnits(),
                product.getImageUrls(),
                product.getSeller().getCompanyName(),
                product.getSubCategory().getId()
        );
    }

    public AddProductDto productToEditProductForm(Product product) {
        return new AddProductDto(
                product.getName(),
                product.getImageUrls(),
                product.getPrice(),
                product.getDescription(),
                product.getNoOfUnits(),
                product.getDetails(),
                product.getCategory().getId(),
                product.getSubCategory().getId(),
                product.getHeight(),
                product.getLength(),
                product.getWidth(),
                product.getWeight()
        );
    }

    public OrderResponseDto orderToOrderResponseDto(Order order) {
        ProductCardResponseDto product = productsToProductsCardResponse(order.getProduct());
        AddressResponseDto deliveryAddress = AddressResponseDto.from(order.getDeliveryAddress());
        ShipmentTrackingDto shipmentTrackingDto = shipmentService.trackOrder(order);
        return new OrderResponseDto(
                order.getId(),
                product,
                order.getQuantity(),
                order.getOrderAmount(),
                deliveryAddress,
                order.getAwbNumber(),
                order.getCourierName(),
                order.getShipmentLabelUrl(),
                shipmentTrackingDto.getStatus(),
                order.getShipmentId(),
                shipmentTrackingDto.getEdd()
        );
    }
}
