package com.ticketing.payment.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRequest() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setTierId(UUID.randomUUID());
        request.setQuantity(2);

        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullTierId() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setTierId(null);
        request.setQuantity(2);

        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Tier ID is required", violations.iterator().next().getMessage());
    }

    @Test
    void testNullQuantity() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setTierId(UUID.randomUUID());
        request.setQuantity(null);

        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Quantity is required", violations.iterator().next().getMessage());
    }

    @Test
    void testQuantityLessThanOne() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setTierId(UUID.randomUUID());
        request.setQuantity(0);

        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Quantity must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    void testConstructorWithParameters() {
        UUID tierId = UUID.randomUUID();
        CreateOrderRequest request = new CreateOrderRequest(tierId, 3);

        assertEquals(tierId, request.getTierId());
        assertEquals(3, request.getQuantity());
    }

    @Test
    void testSettersAndGetters() {
        CreateOrderRequest request = new CreateOrderRequest();
        UUID tierId = UUID.randomUUID();

        request.setTierId(tierId);
        request.setQuantity(5);

        assertEquals(tierId, request.getTierId());
        assertEquals(5, request.getQuantity());
    }

    @Test
    void testNoArgsConstructor() {
        CreateOrderRequest request = new CreateOrderRequest();
        assertNotNull(request);
    }
}
