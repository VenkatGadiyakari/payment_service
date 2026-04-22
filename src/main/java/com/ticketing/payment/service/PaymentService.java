package com.ticketing.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.ticketing.payment.entity.*;
import com.ticketing.payment.exception.DuplicateEventException;
import com.ticketing.payment.exception.InvalidWebhookSignatureException;
import com.ticketing.payment.exception.OrderNotFoundException;
import com.ticketing.payment.repository.OrderItemRepository;
import com.ticketing.payment.repository.OrderRepository;
import com.ticketing.payment.repository.PaymentEventRepository;
import com.ticketing.payment.repository.PaymentRepository;
import com.ticketing.payment.repository.TicketTierRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TicketTierRepository ticketTierRepository;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentEventRepository paymentEventRepository,
                          OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository,
                          TicketTierRepository ticketTierRepository,
                          AuditService auditService,
                          ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentEventRepository = paymentEventRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.ticketTierRepository = ticketTierRepository;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processWebhook(String payload, String signatureHeader) {
        Event event = verifyWebhookSignature(payload, signatureHeader);

        auditService.logWebhookReceived(event.getId(), event.getType());

        if (paymentEventRepository.existsByStripeEventId(event.getId())) {
            auditService.logDuplicateWebhook(event.getId());
            throw new DuplicateEventException("Event already processed: " + event.getId());
        }

        String eventType = event.getType();

        if ("checkout.session.completed".equals(eventType)) {
            handlePaymentSuccess(event, payload);
        } else if ("checkout.session.async_payment_failed".equals(eventType)) {
            handlePaymentFailure(event, payload);
        }
    }

    private Event verifyWebhookSignature(String payload, String signatureHeader) {
        try {
            return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            auditService.logWebhookSignatureFailure(e.getMessage());
            throw new InvalidWebhookSignatureException("Invalid webhook signature: " + e.getMessage());
        }
    }

    private void handlePaymentSuccess(Event event, String rawPayload) {
        Session session = extractSessionFromEvent(event);
        UUID orderId = UUID.fromString(session.getMetadata().get("orderId"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            auditService.logOrderStatusUpdated(
                    orderId.toString(),
                    order.getStatus().name(),
                    order.getStatus().name());
            return;
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            throw new OrderNotFoundException("Order items not found for order: " + orderId);
        }

        OrderItem orderItem = orderItems.get(0);
        int rowsUpdated = ticketTierRepository.decrementInventory(orderItem.getTierId(), orderItem.getQuantity());

        BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal()).divide(BigDecimal.valueOf(100));
        String paymentIntentId = session.getPaymentIntent() != null ? session.getPaymentIntent() : session.getId();

        if (rowsUpdated == 1) {
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setStripePaymentId(paymentIntentId);
            payment.setAmount(amount);
            payment.setCurrency(session.getCurrency() != null ? session.getCurrency().toUpperCase() : "INR");
            payment.setStatus(PaymentStatus.SUCCEEDED);
            payment = paymentRepository.save(payment);

            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);

            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setPaymentId(payment.getId());
            paymentEvent.setStripeEventId(event.getId());
            paymentEvent.setEventType(event.getType());
            paymentEvent.setPayload(rawPayload);
            paymentEventRepository.save(paymentEvent);

            auditService.logInventoryDecremented(orderItem.getTierId().toString(), orderItem.getQuantity());
            auditService.logPaymentSuccess(orderId.toString(), payment.getId().toString(), amount.toString());
            auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), OrderStatus.CONFIRMED.name());
        } else {
            auditService.logOversellDetected(orderId.toString(), orderItem.getTierId().toString());

            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setStripePaymentId(paymentIntentId);
            payment.setAmount(amount);
            payment.setCurrency(session.getCurrency() != null ? session.getCurrency().toUpperCase() : "INR");
            payment.setStatus(PaymentStatus.FAILED);
            payment = paymentRepository.save(payment);

            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);

            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setPaymentId(payment.getId());
            paymentEvent.setStripeEventId(event.getId());
            paymentEvent.setEventType(event.getType());
            paymentEvent.setPayload(rawPayload);
            paymentEventRepository.save(paymentEvent);

            auditService.logPaymentFailed(orderId.toString(), "Concurrent oversell detected");
            auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), OrderStatus.FAILED.name());
        }
    }

    private void handlePaymentFailure(Event event, String rawPayload) {
        Session session = extractSessionFromEvent(event);
        UUID orderId = UUID.fromString(session.getMetadata().get("orderId"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            return;
        }

        BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal()).divide(BigDecimal.valueOf(100));
        String paymentIntentId = session.getPaymentIntent() != null ? session.getPaymentIntent() : session.getId();

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setStripePaymentId(paymentIntentId);
        payment.setAmount(amount);
        payment.setCurrency(session.getCurrency() != null ? session.getCurrency().toUpperCase() : "INR");
        payment.setStatus(PaymentStatus.FAILED);
        payment = paymentRepository.save(payment);

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setPaymentId(payment.getId());
        paymentEvent.setStripeEventId(event.getId());
        paymentEvent.setEventType(event.getType());
        paymentEvent.setPayload(rawPayload);
        paymentEventRepository.save(paymentEvent);

        auditService.logPaymentFailed(orderId.toString(), "Payment failed: " + session.getPaymentStatus());
        auditService.logOrderStatusUpdated(orderId.toString(), OrderStatus.PENDING.name(), OrderStatus.FAILED.name());
    }

    private Session extractSessionFromEvent(Event event) {
        try {
            return objectMapper.convertValue(event.getDataObjectDeserializer().getObject().orElseThrow(), Session.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract session from event", e);
        }
    }
}
