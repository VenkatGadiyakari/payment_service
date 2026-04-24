package com.ticketing.payment.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceClient.class);

    private final RestClient orderServiceRestClient;

    public OrderServiceClient(RestClient orderServiceRestClient) {
        this.orderServiceRestClient = orderServiceRestClient;
    }

    public void confirmOrder(UUID orderId, String paymentReferenceId) {
        try {
            orderServiceRestClient.post()
                    .uri("/api/orders/{id}/confirm", orderId)
                    .body(Map.of("paymentReferenceId", paymentReferenceId))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            logger.error("Failed to confirm order {} in Order Service: {}", orderId, e.getMessage());
        }
    }

    public void failOrder(UUID orderId, String reason) {
        try {
            orderServiceRestClient.post()
                    .uri("/api/orders/{id}/fail", orderId)
                    .body(Map.of("reason", reason != null ? reason : "Payment failed"))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            logger.error("Failed to fail order {} in Order Service: {}", orderId, e.getMessage());
        }
    }
}
