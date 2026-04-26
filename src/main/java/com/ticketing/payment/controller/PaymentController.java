package com.ticketing.payment.controller;

import com.ticketing.payment.dto.CreatePaymentOrderRequest;
import com.ticketing.payment.dto.CreatePaymentOrderResponse;
import com.ticketing.payment.dto.WebhookResponse;
import com.ticketing.payment.exception.DuplicateEventException;
import com.ticketing.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Webhooks", description = "Razorpay webhook receiver — verifies signature and persists payment events")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreatePaymentOrderResponse> createOrder(
            @Valid @RequestBody CreatePaymentOrderRequest request) {

        CreatePaymentOrderResponse response = paymentService.createRazorpayOrder(request.getOrderId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Razorpay webhook",
            description = "Receives Razorpay payment events, verifies the HMAC-SHA256 signature, " +
                    "persists a PaymentEvent record, and updates Payment status. " +
                    "Duplicate events (same razorpay_event_id) are silently acknowledged.")
    @ApiResponse(responseCode = "200", description = "Webhook acknowledged")
    @PostMapping("/webhook")
    public ResponseEntity<WebhookResponse> handleWebhook(
            @RequestBody String payload,
            @Parameter(description = "HMAC-SHA256 signature from Razorpay") @RequestHeader("X-Razorpay-Signature") String signatureHeader) {

        try {
            paymentService.processWebhook(payload, signatureHeader);
        } catch (DuplicateEventException e) {
            return ResponseEntity.ok(new WebhookResponse(true));
        }

        return ResponseEntity.ok(new WebhookResponse(true));
    }
}
