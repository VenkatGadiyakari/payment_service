package com.ticketing.payment.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        ErrorResponse response = new ErrorResponse();

        assertNotNull(response);
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        String errorCode = "TEST_ERROR";
        String message = "Test error message";
        String traceId = "trace-123";

        ErrorResponse response = new ErrorResponse(errorCode, message, traceId);

        assertEquals(errorCode, response.getErrorCode());
        assertEquals(message, response.getMessage());
        assertEquals(traceId, response.getTraceId());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponse response = new ErrorResponse();

        response.setErrorCode("VALIDATION_ERROR");
        response.setMessage("Validation failed");
        response.setTraceId("trace-456");
        response.setTimestamp("2026-04-21T10:00:00Z");

        assertEquals("VALIDATION_ERROR", response.getErrorCode());
        assertEquals("Validation failed", response.getMessage());
        assertEquals("trace-456", response.getTraceId());
        assertEquals("2026-04-21T10:00:00Z", response.getTimestamp());
    }

    @Test
    void testTimestampAutoGeneration() {
        ErrorResponse response1 = new ErrorResponse();
        ErrorResponse response2 = new ErrorResponse("ERROR", "Message", "trace");

        assertNotNull(response1.getTimestamp());
        assertNotNull(response2.getTimestamp());
    }
}
