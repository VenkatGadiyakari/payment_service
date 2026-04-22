package com.ticketing.payment.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testInvalidTierException() {
        String message = "Tier is not active";
        InvalidTierException exception = new InvalidTierException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testInsufficientInventoryException() {
        String message = "Insufficient inventory available";
        InsufficientInventoryException exception = new InsufficientInventoryException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testInvalidWebhookSignatureException() {
        String message = "Invalid webhook signature";
        InvalidWebhookSignatureException exception = new InvalidWebhookSignatureException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testDuplicateEventException() {
        String message = "Event already processed";
        DuplicateEventException exception = new DuplicateEventException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testOrderNotFoundException() {
        String message = "Order not found";
        OrderNotFoundException exception = new OrderNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionsWithNullMessage() {
        InvalidTierException ex1 = new InvalidTierException(null);
        InsufficientInventoryException ex2 = new InsufficientInventoryException(null);
        InvalidWebhookSignatureException ex3 = new InvalidWebhookSignatureException(null);
        DuplicateEventException ex4 = new DuplicateEventException(null);
        OrderNotFoundException ex5 = new OrderNotFoundException(null);

        assertNull(ex1.getMessage());
        assertNull(ex2.getMessage());
        assertNull(ex3.getMessage());
        assertNull(ex4.getMessage());
        assertNull(ex5.getMessage());
    }

    @Test
    void testExceptionCanBeCaught() {
        try {
            throw new InvalidTierException("Test exception");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
            assertTrue(e instanceof InvalidTierException);
        }
    }
}
