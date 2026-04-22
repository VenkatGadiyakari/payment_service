package com.ticketing.payment.exception;

import com.ticketing.payment.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidTierException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTierException(InvalidTierException ex) {
        String traceId = MDC.get("traceId");
        logger.error("Invalid tier exception occurred", ex);
        ErrorResponse error = new ErrorResponse("INVALID_TIER", ex.getMessage(), traceId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientInventoryException(InsufficientInventoryException ex) {
        String traceId = MDC.get("traceId");
        logger.error("Insufficient inventory exception occurred", ex);
        ErrorResponse error = new ErrorResponse("INSUFFICIENT_INVENTORY", ex.getMessage(), traceId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidWebhookSignatureException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWebhookSignatureException(InvalidWebhookSignatureException ex) {
        String traceId = MDC.get("traceId");
        logger.error("Invalid webhook signature exception occurred - SECURITY ALERT", ex);
        ErrorResponse error = new ErrorResponse("INVALID_WEBHOOK_SIGNATURE", ex.getMessage(), traceId);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateEventException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEventException(DuplicateEventException ex) {
        String traceId = MDC.get("traceId");
        logger.warn("Duplicate event detected: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("DUPLICATE_EVENT", ex.getMessage(), traceId);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        String traceId = MDC.get("traceId");
        logger.error("Order not found exception occurred", ex);
        ErrorResponse error = new ErrorResponse("ORDER_NOT_FOUND", ex.getMessage(), traceId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String traceId = MDC.get("traceId");
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        logger.error("Validation exception occurred: {}", message);
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", message, traceId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        String traceId = MDC.get("traceId");
        logger.error("Unexpected exception occurred", ex);
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", traceId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
