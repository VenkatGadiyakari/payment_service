package com.ticketing.payment.dto;

import java.time.Instant;

public class ErrorResponse {

    private String errorCode;
    private String message;
    private String timestamp;
    private String traceId;

    public ErrorResponse() {
        this.timestamp = Instant.now().toString();
    }

    public ErrorResponse(String errorCode, String message, String traceId) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = Instant.now().toString();
        this.traceId = traceId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
