package com.ticketing.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.payment.client.OrderServiceClient;
import com.ticketing.payment.dto.CreatePaymentOrderRequest;
import com.ticketing.payment.dto.CreatePaymentOrderResponse;
import com.ticketing.payment.entity.*;
import com.ticketing.payment.exception.DuplicateEventException;
import com.ticketing.payment.exception.InvalidWebhookSignatureException;
import com.ticketing.payment.exception.OrderNotFoundException;
import com.ticketing.payment.repository.OrderRepository;
import com.ticketing.payment.repository.PaymentEventRepository;
import com.ticketing.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final OrderRepository orderRepository;
    private final RazorpayService razorpayService;
    private final OrderServiceClient orderServiceClient;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentEventRepository paymentEventRepository,
                          OrderRepository orderRepository,
                          RazorpayService razorpayService,
                          OrderServiceClient orderServiceClient,
                          AuditService auditService,
                          ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentEventRepository = paymentEventRepository;
        this.orderRepository = orderRepository;
        this.razorpayService = razorpayService;
        this.orderServiceClient = orderServiceClient;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CreatePaymentOrderResponse createRazorpayOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not in PENDING status: " + order.getStatus());
        }

        long amountInPaise = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue();
        String razorpayOrderId = razorpayService.createOrder(orderId, amountInPaise);

        auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), "RAZORPAY_ORDER_CREATED");

        return new CreatePaymentOrderResponse(
                razorpayOrderId,
                amountInPaise,
                "INR",
                razorpayService.getKeyId()
        );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processWebhook(String payload, String signatureHeader) {
        try {
            razorpayService.verifyWebhookSignature(payload, signatureHeader);
        } catch (RuntimeException e) {
            auditService.logWebhookSignatureFailure(e.getMessage());
            throw new InvalidWebhookSignatureException("Webhook signature verification failed: " + e.getMessage());
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse webhook payload", e);
        }

        String eventType = root.path("event").asText();
        String razorpayPaymentId = root.path("payload").path("payment").path("entity").path("id").asText("");

        auditService.logWebhookReceived(razorpayPaymentId, eventType);

        if (paymentEventRepository.existsByRazorpayEventId(razorpayPaymentId)) {
            auditService.logDuplicateWebhook(razorpayPaymentId);
            throw new DuplicateEventException("Event already processed: " + razorpayPaymentId);
        }

        if ("payment.captured".equals(eventType)) {
            handlePaymentSuccess(root, razorpayPaymentId, payload, eventType);
        } else if ("payment.failed".equals(eventType)) {
            handlePaymentFailure(root, razorpayPaymentId, payload, eventType);
        }
    }

    private void handlePaymentSuccess(JsonNode root, String razorpayPaymentId, String rawPayload, String eventType) {
        JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
        JsonNode notes = paymentEntity.path("notes");

        String orderIdStr = notes.path("orderId").asText("");
        if (orderIdStr.isEmpty()) {
            throw new RuntimeException("Missing orderId in webhook notes");
        }
        UUID orderId = UUID.fromString(orderIdStr);

        String razorpayOrderId = paymentEntity.path("order_id").asText("");
        long amountInPaise = paymentEntity.path("amount").asLong(0);
        BigDecimal amount = BigDecimal.valueOf(amountInPaise).divide(BigDecimal.valueOf(100));
        String currency = paymentEntity.path("currency").asText("INR").toUpperCase();

        orderServiceClient.confirmOrder(orderId, razorpayPaymentId);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment = paymentRepository.save(payment);

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setPaymentId(payment.getId());
        paymentEvent.setRazorpayEventId(razorpayPaymentId);
        paymentEvent.setEventType(eventType);
        paymentEvent.setPayload(rawPayload);
        paymentEventRepository.save(paymentEvent);

        auditService.logPaymentSuccess(orderId.toString(), payment.getId().toString(), amount.toString());
        auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), OrderStatus.CONFIRMED.name());
    }

    private void handlePaymentFailure(JsonNode root, String razorpayPaymentId, String rawPayload, String eventType) {
        JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
        JsonNode notes = paymentEntity.path("notes");

        String orderIdStr = notes.path("orderId").asText("");
        if (orderIdStr.isEmpty()) {
            throw new RuntimeException("Missing orderId in webhook notes");
        }
        UUID orderId = UUID.fromString(orderIdStr);

        String razorpayOrderId = paymentEntity.path("order_id").asText("");
        long amountInPaise = paymentEntity.path("amount").asLong(0);
        BigDecimal amount = BigDecimal.valueOf(amountInPaise).divide(BigDecimal.valueOf(100));
        String currency = paymentEntity.path("currency").asText("INR").toUpperCase();
        String errorDescription = paymentEntity.path("error_description").asText("Payment failed");

        orderServiceClient.failOrder(orderId, errorDescription);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.FAILED);
        payment = paymentRepository.save(payment);

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setPaymentId(payment.getId());
        paymentEvent.setRazorpayEventId(razorpayPaymentId);
        paymentEvent.setEventType(eventType);
        paymentEvent.setPayload(rawPayload);
        paymentEventRepository.save(paymentEvent);

        auditService.logPaymentFailed(orderId.toString(), "Payment failed: " + errorDescription);
        auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), OrderStatus.FAILED.name());
    }
}
