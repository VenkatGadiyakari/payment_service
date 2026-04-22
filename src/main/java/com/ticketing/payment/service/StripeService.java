package com.ticketing.payment.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createCheckoutSession(UUID orderId, BigDecimal totalAmount, String tierName,
                                        Integer quantity, String successUrl, String cancelUrl) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("orderId", orderId.toString());

            long amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(amountInPaise / quantity)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(tierName)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(Long.valueOf(quantity))
                                    .build()
                    )
                    .putAllMetadata(metadata)
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe checkout session: " + e.getMessage(), e);
        }
    }

    public String extractSessionId(String checkoutUrl) {
        if (checkoutUrl != null && checkoutUrl.contains("/pay/")) {
            int index = checkoutUrl.lastIndexOf("/pay/");
            return checkoutUrl.substring(index + 5);
        }
        return null;
    }
}
