package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testNoArgsConstructor() {
        OrderItem orderItem = new OrderItem();
        assertNotNull(orderItem);
    }

    @Test
    void testAllArgsConstructor() {
        UUID orderId = UUID.randomUUID();
        UUID tierId = UUID.randomUUID();
        Integer quantity = 3;
        String tierName = "VIP Pass";
        String eventTitle = "Concert 2026";
        LocalDateTime eventDate = LocalDateTime.of(2026, 6, 15, 19, 0);
        BigDecimal unitPrice = BigDecimal.valueOf(150.00);

        OrderItem orderItem = new OrderItem(orderId, tierId, quantity, tierName, eventTitle, eventDate, unitPrice);

        assertEquals(orderId, orderItem.getOrderId());
        assertEquals(tierId, orderItem.getTierId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(tierName, orderItem.getTierName());
        assertEquals(eventTitle, orderItem.getEventTitle());
        assertEquals(eventDate, orderItem.getEventDate());
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    void testSettersAndGetters() {
        OrderItem orderItem = new OrderItem();
        UUID id = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID tierId = UUID.randomUUID();
        Integer quantity = 5;
        String tierName = "General Admission";
        String eventTitle = "Festival 2026";
        LocalDateTime eventDate = LocalDateTime.of(2026, 8, 20, 14, 30);
        BigDecimal unitPrice = BigDecimal.valueOf(75.50);

        orderItem.setId(id);
        orderItem.setOrderId(orderId);
        orderItem.setTierId(tierId);
        orderItem.setQuantity(quantity);
        orderItem.setTierName(tierName);
        orderItem.setEventTitle(eventTitle);
        orderItem.setEventDate(eventDate);
        orderItem.setUnitPrice(unitPrice);

        assertEquals(id, orderItem.getId());
        assertEquals(orderId, orderItem.getOrderId());
        assertEquals(tierId, orderItem.getTierId());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(tierName, orderItem.getTierName());
        assertEquals(eventTitle, orderItem.getEventTitle());
        assertEquals(eventDate, orderItem.getEventDate());
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    void testNullableFields() {
        OrderItem orderItem = new OrderItem();

        orderItem.setTierName(null);
        orderItem.setEventTitle(null);
        orderItem.setEventDate(null);
        orderItem.setUnitPrice(null);

        assertNull(orderItem.getTierName());
        assertNull(orderItem.getEventTitle());
        assertNull(orderItem.getEventDate());
        assertNull(orderItem.getUnitPrice());
    }
}
