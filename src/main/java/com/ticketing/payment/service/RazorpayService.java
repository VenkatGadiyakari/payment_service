package com.ticketing.payment.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() {
        try {
            System.out.println("KEY_ID: " + keyId);
            System.out.println("KEY_SECRET: " + keySecret);
            razorpayClient = new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to initialize Razorpay client", e);
        }
    }

    public String createOrder(UUID internalOrderId, long amountInPaise) {
        try {
            JSONObject notes = new JSONObject();
            notes.put("orderId", internalOrderId.toString());

            JSONObject request = new JSONObject();
            request.put("amount", amountInPaise);
            request.put("currency", "INR");
            request.put("receipt", internalOrderId.toString());
            request.put("notes", notes);

            com.razorpay.Order order = razorpayClient.orders.create(request);
            return order.get("id");
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    public void verifyWebhookSignature(String payload, String signature) {
        try {
            boolean valid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
            if (!valid) {
                throw new RuntimeException("Invalid Razorpay webhook signature");
            }
        } catch (RazorpayException e) {
            throw new RuntimeException("Webhook signature verification failed: " + e.getMessage(), e);
        }
    }

    public String getKeyId() {
        return keyId;
    }
}
