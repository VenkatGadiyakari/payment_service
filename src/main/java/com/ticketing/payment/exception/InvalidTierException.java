package com.ticketing.payment.exception;

public class InvalidTierException extends RuntimeException {

    public InvalidTierException(String message) {
        super(message);
    }
}
