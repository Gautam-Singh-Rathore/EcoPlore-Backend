package com.greenplore.Backend.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenplore.Backend.order_service.dto.ShipmentRequestDto;
import com.greenplore.Backend.order_service.dto.ShipmentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ShipmentService {

    @Value("${nimbus.token}")
    private String nimbusToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ShipmentService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ShipmentResponseDto createShipment(ShipmentRequestDto shipmentRequest) {
        String url = "https://api.nimbuspost.com/v1/shipments";

        try {
            // Log the request payload for debugging
            String requestJson = objectMapper.writeValueAsString(shipmentRequest);
            System.out.println("Shipment Request JSON: " + requestJson);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(nimbusToken);
            headers.add("User-Agent", "YourApp/1.0");

            HttpEntity<ShipmentRequestDto> request = new HttpEntity<>(shipmentRequest, headers);

            ResponseEntity<ShipmentResponseDto> response = restTemplate.postForEntity(url, request, ShipmentResponseDto.class);

            System.out.println("API Response Status: " + response.getStatusCode());
            System.out.println("API Response Body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                if (response.getBody().getStatus() == Boolean.TRUE) {
                    return response.getBody();
                } else {
                    System.out.println("API returned status: false");
                    // Log the error details if available
                    throw new RuntimeException("Nimbuspost API returned status: false. Check request data validity.");
                }
            }

            throw new RuntimeException("Failed to create shipment: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Error: " + e.getStatusCode());
            System.out.println("Error Response: " + e.getResponseBodyAsString());
            throw new RuntimeException("HTTP Error while creating shipment: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error while creating shipment: " + e.getMessage());
        }
    }
}
