package com.greenplore.Backend.user_service.service;

import com.greenplore.Backend.order_service.entity.Order;
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

    }
}
