package com.ticketing.payment.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() {
        try {
            razorpayClient = new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to initialize Razorpay client", e);
        }
    }

    public String createPaymentLink(UUID orderId, BigDecimal totalAmount, String tierName,
                                    Integer quantity, String successUrl, String cancelUrl) {
        try {
            long amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();

            JSONObject notes = new JSONObject();
            notes.put("orderId", orderId.toString());

            JSONObject request = new JSONObject();
            request.put("amount", amountInPaise);
            request.put("currency", "INR");
            request.put("description", tierName + " x" + quantity);
            request.put("notes", notes);
            request.put("callback_url", successUrl + "?order_id=" + orderId);
            request.put("callback_method", "get");
            request.put("cancel_url", cancelUrl);

            PaymentLink paymentLink = razorpayClient.paymentLink.create(request);
            return paymentLink.get("short_url");
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay payment link: " + e.getMessage(), e);
        }
    }

    public String extractPaymentLinkId(String checkoutUrl) {
        if (checkoutUrl != null && checkoutUrl.contains("rzp.io/l/")) {
            int index = checkoutUrl.lastIndexOf("/");
            return checkoutUrl.substring(index + 1);
        }
        return null;
    }
}
