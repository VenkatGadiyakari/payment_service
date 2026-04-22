package com.ticketing.payment.dto;

public class WebhookResponse {

    private boolean received;

    public WebhookResponse() {
    }

    public WebhookResponse(boolean received) {
        this.received = received;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
