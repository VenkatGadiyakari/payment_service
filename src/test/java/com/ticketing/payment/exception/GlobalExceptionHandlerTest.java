package com.ticketing.payment.exception;

import com.ticketing.payment.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        MDC.clear();
        MDC.put("traceId", "test-trace-123");
    }

    @Test
    void testHandleInvalidTierException() {
        InvalidTierException exception = new InvalidTierException("Tier is not active");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidTierException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_TIER", response.getBody().getErrorCode());
        assertEquals("Tier is not active", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleInsufficientInventoryException() {
        InsufficientInventoryException exception = new InsufficientInventoryException("Not enough tickets");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInsufficientInventoryException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INSUFFICIENT_INVENTORY", response.getBody().getErrorCode());
        assertEquals("Not enough tickets", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleInvalidWebhookSignatureException() {
        InvalidWebhookSignatureException exception = new InvalidWebhookSignatureException("Invalid signature");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidWebhookSignatureException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_WEBHOOK_SIGNATURE", response.getBody().getErrorCode());
        assertEquals("Invalid signature", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleDuplicateEventException() {
        DuplicateEventException exception = new DuplicateEventException("Event already processed");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateEventException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DUPLICATE_EVENT", response.getBody().getErrorCode());
        assertEquals("Event already processed", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleOrderNotFoundException() {
        OrderNotFoundException exception = new OrderNotFoundException("Order not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ORDER_NOT_FOUND", response.getBody().getErrorCode());
        assertEquals("Order not found", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleGeneralException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_ERROR", response.getBody().getErrorCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals("test-trace-123", response.getBody().getTraceId());
    }

    @Test
    void testHandleExceptionWithoutTraceId() {
        MDC.clear();

        InvalidTierException exception = new InvalidTierException("Test error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidTierException(exception);

        assertNotNull(response.getBody());
        assertNull(response.getBody().getTraceId());
    }

    @Test
    void testErrorResponseContainsTimestamp() {
        InvalidTierException exception = new InvalidTierException("Test error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidTierException(exception);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }
}
