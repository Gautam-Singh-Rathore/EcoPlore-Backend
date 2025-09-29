package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.order_service.entity.Order;
import com.greenplore.Backend.user_service.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOTPEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification - OTP");
        message.setText("Your OTP for email verification is: " + otp +
                "\nThis OTP is valid for 10 minutes only." +
                "\nPlease do not share this OTP with anyone.");
        message.setFrom("noreply@greenplore.com");

        mailSender.send(message);
    }

    public void sendOrderMail(Order newOrder) {
        // Extract seller and customer info
        String sellerEmail = newOrder.getSeller().getUser().getEmail();
        String customerName = newOrder.getCustomer().getFirstName() + " " + newOrder.getCustomer().getLastName();
        String customerMobile = newOrder.getCustomer().getMobileNo();

        // Extract product and delivery info
        String productName = newOrder.getProduct().getName();
        int quantity = newOrder.getQuantity();
        double totalAmount = newOrder.getOrderAmount();

        // Address info
        String address = formatAddress(newOrder.getDeliveryAddress());

        // Shipment info
        String shipmentInfo = (newOrder.getAwbNumber() != null && newOrder.getCourierName() != null)
                ? "\nShipment via " + newOrder.getCourierName() +
                "\nAWB Number: " + newOrder.getAwbNumber() +
                "\nStatus: " + newOrder.getShipmentStatus()
                : "\nShipment details are not yet available.";

        // Order Info
        String orderDetails = "New Order Received\n\n"
                + "Order ID: " + newOrder.getId()
                + "\nProduct: " + productName
                + "\nQuantity: " + quantity
                + "\nTotal Amount: ₹" + totalAmount
                + "\n\nCustomer Details:"
                + "\nName: " + customerName
                + "\nMobile: " + customerMobile
                + "\nDelivery Address: " + address
                + shipmentInfo;

        // Send to Seller
        sendEmail(sellerEmail, "New Order Received - Order ID " + newOrder.getId(), orderDetails);

        // Send to Admin
        String adminEmail = "admin@greenplore.com"; // You can externalize this
        sendEmail(adminEmail, "New Order Notification - Order ID " + newOrder.getId(), orderDetails);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@greenplore.com");

        mailSender.send(message);
    }

    private String formatAddress(Address address) {
        if (address == null) return "N/A";

        return address.getStreet() + ", " +
                address.getCity() + ", " +
                address.getState() + " - " +
                address.getPinCode();
    }


}
