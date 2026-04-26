package com.ticketing.payment.dto;

public class CreatePaymentOrderResponse {

    private String razorpayOrderId;
    private long amount;
    private String currency;
    private String key;

    public CreatePaymentOrderResponse() {
    }

    public CreatePaymentOrderResponse(String razorpayOrderId, long amount, String currency, String key) {
        this.razorpayOrderId = razorpayOrderId;
        this.amount = amount;
        this.currency = currency;
        this.key = key;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
