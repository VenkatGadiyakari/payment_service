package com.ticketing.payment.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketTierTest {

    @Test
    void testNoArgsConstructor() {
        TicketTier tier = new TicketTier();
        assertNotNull(tier);
    }

    @Test
    void testSettersAndGetters() {
        TicketTier tier = new TicketTier();
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        String name = "VIP Tier";
        BigDecimal price = BigDecimal.valueOf(200.00);
        Integer totalQty = 100;
        Integer remainingQty = 75;
        Integer maxPerOrder = 5;
        String eventTitle = "Summer Festival";
        LocalDateTime eventDate = LocalDateTime.of(2026, 7, 15, 18, 0);

        tier.setId(id);
        tier.setEventId(eventId);
        tier.setName(name);
        tier.setPrice(price);
        tier.setTotalQty(totalQty);
        tier.setRemainingQty(remainingQty);
        tier.setStatus(TierStatus.ACTIVE);
        tier.setMaxPerOrder(maxPerOrder);
        tier.setEventTitle(eventTitle);
        tier.setEventDate(eventDate);

        assertEquals(id, tier.getId());
        assertEquals(eventId, tier.getEventId());
        assertEquals(name, tier.getName());
        assertEquals(price, tier.getPrice());
        assertEquals(totalQty, tier.getTotalQty());
        assertEquals(remainingQty, tier.getRemainingQty());
        assertEquals(TierStatus.ACTIVE, tier.getStatus());
        assertEquals(maxPerOrder, tier.getMaxPerOrder());
        assertEquals(eventTitle, tier.getEventTitle());
        assertEquals(eventDate, tier.getEventDate());
    }

    @Test
    void testStatusValues() {
        TicketTier tier = new TicketTier();

        tier.setStatus(TierStatus.ACTIVE);
        assertEquals(TierStatus.ACTIVE, tier.getStatus());

        tier.setStatus(TierStatus.INACTIVE);
        assertEquals(TierStatus.INACTIVE, tier.getStatus());
    }

    @Test
    void testInventoryFields() {
        TicketTier tier = new TicketTier();

        tier.setTotalQty(50);
        tier.setRemainingQty(30);

        assertEquals(50, tier.getTotalQty());
        assertEquals(30, tier.getRemainingQty());
    }
}
