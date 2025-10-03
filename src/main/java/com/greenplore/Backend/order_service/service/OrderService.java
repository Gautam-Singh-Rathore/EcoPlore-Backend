package com.greenplore.Backend.order_service.service;

import com.greenplore.Backend.order_service.dto.CartItemResponseDto;
import com.greenplore.Backend.order_service.dto.OrderResponseDto;
import com.greenplore.Backend.order_service.dto.ShipmentRequestDto;
import com.greenplore.Backend.order_service.dto.ShipmentResponseDto;
import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.order_service.repo.CartRepo;
import com.greenplore.Backend.order_service.repo.OrderItemRepo;
import com.greenplore.Backend.order_service.repo.OrderRepo;
import com.greenplore.Backend.product_service.entity.Product;
import com.greenplore.Backend.product_service.exception.CustomerNotFound;
import com.greenplore.Backend.product_service.exception.ProductNotFoundException;
import com.greenplore.Backend.product_service.repo.ProductRepo;
import com.greenplore.Backend.product_service.service.Mapper;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.greenplore.Backend.user_service.entity.Address;
import com.greenplore.Backend.user_service.entity.Customer;
import com.greenplore.Backend.user_service.entity.Seller;
import com.greenplore.Backend.user_service.entity.User;
import com.greenplore.Backend.user_service.exception.UserNotFoundException;
import com.greenplore.Backend.user_service.repo.AddressRepo;
import com.greenplore.Backend.user_service.repo.CustomerRepo;
import com.greenplore.Backend.user_service.repo.SellerRepo;
import com.greenplore.Backend.user_service.repo.UserRepo;
import com.greenplore.Backend.user_service.service.EmailService;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private Mapper mapper;
    @Autowired
    private EmailService emailService;

    public HashMap<String , String> checkOrderQuantity(List<CartItemResponseDto> items ){
        for(CartItemResponseDto item : items){
            if(productRepo.findById(item.productId()).isPresent()){
                Product product = productRepo.findById(item.productId()).get();
                if(product.getNoOfUnits() < item.quantity()){
                    HashMap<String , String> response = new HashMap<>();
                    response.put("status" , "false");
                    response.put("msg" , item.productName()+" has only "+product.getNoOfUnits()+" units available" );
                    return  response;
                }
            }else{
                HashMap<String , String> response = new HashMap<>();
                response.put("status" , "false");
                response.put("msg" , item.productName()+" no longer exist. Please remove it from the cart to place your order" );
                return  response;
            }
//
        }
        HashMap<String , String> response = new HashMap<>();
        response.put("status" , "true");
        response.put("msg" , "Good to go " );
        return  response;
    }

    @Transactional
    public String createOrders(UserDetailsImpl user, List<CartItemResponseDto> items, Long addressId) {
        List<Order> createdOrders = new ArrayList<>();
        User custUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found for email "+user.getUsername()));
        Customer customer = customerRepo.findByUser(custUser)
                .orElseThrow(()-> new CustomerNotFound("Customer Not Found For email "+user.getUsername()));
        Address deliverAddress = addressRepo.findById(addressId)
                .orElseThrow(()-> new RuntimeException("Address not found"));
        for(CartItemResponseDto item : items){
            // Create Order
            Product product = productRepo.findByIdAndIsDeletedFalse(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
            Order newOrder = Order.builder()
                    .product(product)
                    .quantity(item.quantity())
                    .customer(customer)
                    .seller(product.getSeller())
                    .orderAmount(50+ product.getPrice()* item.quantity())
                    .deliveryAddress(deliverAddress)
                    .build();

            // Create Shipment
            try {
                ShipmentRequestDto shipmentRequest = createShipmentRequest(product,item.quantity() , customer , deliverAddress , product.getSeller());
                ShipmentResponseDto shipmentResponse = shipmentService.createShipment(shipmentRequest);
                if (shipmentResponse.getStatus() && shipmentResponse.getData() != null) {
                    ShipmentResponseDto.ShipmentData data = shipmentResponse.getData();
                    newOrder.setAwbNumber(data.getAwb_number());
                    newOrder.setCourierName(data.getCourier_name());
                    newOrder.setShipmentLabelUrl(data.getLabel());
                    newOrder.setShipmentStatus(data.getStatus());
                    newOrder.setShipmentId(data.getShipment_id());
                }
                Order savedOrder = orderRepo.save(newOrder);
                createdOrders.add(savedOrder);

                // Decrease the quantity when order is made
                product.setNoOfUnits(product.getNoOfUnits()- newOrder.getQuantity());
                productRepo.save(product);

                // Send email for created order to seller and customer
                emailService.sendOrderMail(newOrder);
            }catch (Exception e){
                throw new RuntimeException("Failed to create shipment for order: " + e.getMessage());
            }
        }

        // empty the cart
        for(CartItemResponseDto item : items){
            orderItemRepo.deleteById(item.cartItemId());
        }
        return "Orders created successfully with shipments";
    }


    private ShipmentRequestDto createShipmentRequest(Product product, int quantity, Customer customer, Address deliverAddress, Seller seller) {
    // Create order items
    List<ShipmentRequestDto.OrderItem> items = List.of(
            ShipmentRequestDto.OrderItem.builder()
                    .name(product.getName())
                    .qty(String.valueOf(quantity))
                    .price(String.valueOf(product.getPrice().intValue())) // Ensure integer value
                    .sku("default-sku")
                    .build()
    );

    // Create consignee - fix field mapping
    ShipmentRequestDto.Consignee consignee = ShipmentRequestDto.Consignee.builder()
            .name(customer.getFirstName() + " " + customer.getLastName())
            .address(deliverAddress.getStreet())
            .address_2(deliverAddress.getCity() != null ? deliverAddress.getCity() : "") // Use landmark for address_2
            .city(deliverAddress.getCity())
            .state(deliverAddress.getState())
            .pincode(deliverAddress.getPinCode())
            .phone(customer.getMobileNo())
            .build();

    // Create pickup
    ShipmentRequestDto.Pickup pickup = ShipmentRequestDto.Pickup.builder()
            .warehouse_name(seller.getCompanyName()) // Fix apostrophe
            .name(seller.getCompanyName())
            .address(seller.getPickUpAddress().getBuildingNo() + " " + seller.getPickUpAddress().getStreet())
            .address_2(seller.getPickUpAddress().getLandmark() != null ? seller.getPickUpAddress().getLandmark() : "")
            .city(seller.getPickUpAddress().getCity())
            .state(seller.getPickUpAddress().getState())
            .pincode(seller.getPickUpAddress().getPinCode())
            .phone(seller.getMobileNo())
            .build();

    return ShipmentRequestDto.builder()
            .order_number(customer.getFirstName() + String.valueOf(System.currentTimeMillis()))
            .payment_type("prepaid")
            .order_amount((double) (50 + product.getPrice() * quantity)) // Ensure double type
            .package_length(product.getLength() != null ? product.getLength() : 10) // Provide defaults
            .package_breadth(product.getWidth() != null ? product.getWidth() : 10)
            .package_height(product.getHeight() != null ? product.getHeight() : 10)
            .package_weight(product.getWeight() != null ? product.getWeight() : 300)
            .order_items(items)
            .consignee(consignee)
            .pickup(pickup)
            .build();
}



    // Get Seller Orders
    public List<OrderResponseDto> getSellerOrders(UserDetailsImpl user) {
        User myUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found.."));
        Seller seller = sellerRepo.findByUser(myUser)
                .orElseThrow(()-> new CustomerNotFound("Seller not found.."));

        List<Order> orders = orderRepo.findBySeller(seller);
        return orders.stream()
                .map(mapper::orderToOrderResponseDto)
                .collect(Collectors.toList());
    }

    // Get Customer Orders
    public List<OrderResponseDto> getCustomerOrders(UserDetailsImpl user) {
        User myUser = userRepo.findByEmail(user.getUsername())
                .orElseThrow(()-> new UserNotFoundException("User not found.."));
        Customer customer = customerRepo.findByUser(myUser)
                .orElseThrow(()-> new CustomerNotFound("Customer not found.."));

        List<Order> orders = orderRepo.findByCustomer(customer);
        return orders.stream()
                .map(mapper::orderToOrderResponseDto)
                .collect(Collectors.toList());
    }


}
