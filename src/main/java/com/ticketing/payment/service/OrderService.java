package com.ticketing.payment.service;

import com.ticketing.payment.dto.CreateOrderRequest;
import com.ticketing.payment.dto.CreateOrderResponse;
import com.ticketing.payment.entity.Order;
import com.ticketing.payment.entity.OrderItem;
import com.ticketing.payment.entity.OrderStatus;
import com.ticketing.payment.entity.TicketTier;
import com.ticketing.payment.entity.TierStatus;
import com.ticketing.payment.exception.InsufficientInventoryException;
import com.ticketing.payment.exception.InvalidTierException;
import com.ticketing.payment.repository.OrderItemRepository;
import com.ticketing.payment.repository.OrderRepository;
import com.ticketing.payment.repository.TicketTierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TicketTierRepository ticketTierRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        TicketTierRepository ticketTierRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.ticketTierRepository = ticketTierRepository;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, UUID buyerId) {
        TicketTier tier = ticketTierRepository.findById(request.getTierId())
                .orElseThrow(() -> new InvalidTierException("Tier not found: " + request.getTierId()));

        if (tier.getStatus() != TierStatus.ACTIVE) {
            throw new InvalidTierException("Tier is not active: " + request.getTierId());
        }

        if (tier.getRemainingQty() < request.getQuantity()) {
            throw new InsufficientInventoryException("Insufficient inventory for tier: " + request.getTierId());
        }

        if (request.getQuantity() > tier.getMaxPerOrder()) {
            throw new InvalidTierException("Quantity exceeds max per order limit of " + tier.getMaxPerOrder());
        }

        int updated = ticketTierRepository.decrementInventory(request.getTierId(), request.getQuantity());
        if (updated == 0) {
            throw new InsufficientInventoryException("Insufficient inventory for tier: " + request.getTierId());
        }

        Instant now = Instant.now();
        BigDecimal totalAmount = tier.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setBuyerId(buyerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        orderRepository.save(order);

        OrderItem item = new OrderItem(order.getId(), tier.getId(), request.getQuantity(),
                tier.getName(), tier.getEventTitle(), tier.getEventDate(), tier.getPrice());
        orderItemRepository.save(item);

        return new CreateOrderResponse(order.getId(), order.getBuyerId(), order.getStatus(),
                order.getTotalAmount(), order.getCreatedAt());
    }
}
