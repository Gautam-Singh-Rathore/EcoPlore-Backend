package com.greenplore.Backend.order_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class NimbusTokenService {

    @Value("${nimbus.username}")
    private String username;

    @Value("${nimbus.password}")
    private String password;

    private String token;
    private Instant expiryTime;

    private final RestTemplate restTemplate = new RestTemplate();

    public synchronized void refreshToken(){
        Map<String, String> payload = new HashMap<>();
        payload.put("email", username);
        payload.put("password", password);

        Map response = restTemplate.postForObject(
                "https://api.nimbuspost.com/v1/users/login", payload, Map.class);

        if (response != null && response.containsKey("data")) {
            this.token = (String) response.get("data");
            this.expiryTime = Instant.now().plus(Duration.ofHours(3));
            System.out.println("Nimbus Token refreshed successfully!");
        } else {
            throw new RuntimeException("Failed to fetch Nimbus Post token");
        }
    }

    public synchronized String getToken(){
        if (token == null || Instant.now().isAfter(expiryTime)) {
            refreshToken();
        }
        return token;
    }

    @Scheduled(fixedRate = 10500000)
    public void scheduledRefresh(){
        refreshToken();
    }
}
