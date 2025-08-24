package com.greenplore.Backend.order_service.controller;

import com.greenplore.Backend.order_service.dto.CreateOrderDto;
import com.greenplore.Backend.order_service.dto.PaymentVerificationRequest;
import com.greenplore.Backend.order_service.service.OrderService;
import com.greenplore.Backend.user_service.auth.UserDetailsImpl;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/private/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Value("${rzp.key-id}")
    private String keyId;

    @Value("${rzp.key-secret}")
    private String secret;

    @GetMapping("/payment/{amount}")
    public String Payment(@PathVariable String amount) throws RazorpayException {

        RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt_11");

        Order order = razorpayClient.orders.create(orderRequest);
        String orderId = order.get("id");

        return orderId;
    }

    @PostMapping("/payment/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentVerificationRequest request
            ){
        try {
            String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
            String actualSignature = hmacSHA256(payload, secret);

            if (actualSignature.equals(request.getRazorpaySignature())) {
                return ResponseEntity.ok("Payment verification successful");
            } else {
                return ResponseEntity.badRequest().body("Payment verification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while verifying payment: " + e.getMessage());
        }
    }
    // Helper Function
    private String hmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return new String(Base64.getEncoder().encode(hash));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestBody CreateOrderDto orderDto
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.createOrders(user , orderDto.items() , orderDto.addressId()));
    }
}
