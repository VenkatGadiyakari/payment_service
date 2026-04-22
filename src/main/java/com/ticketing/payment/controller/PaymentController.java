package com.ticketing.payment.controller;

import com.ticketing.payment.dto.WebhookResponse;
import com.ticketing.payment.exception.DuplicateEventException;
import com.ticketing.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<WebhookResponse> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader) {

        try {
            paymentService.processWebhook(payload, signatureHeader);
        } catch (DuplicateEventException e) {
            return ResponseEntity.ok(new WebhookResponse(true));
        }

        return ResponseEntity.ok(new WebhookResponse(true));
    }
}
