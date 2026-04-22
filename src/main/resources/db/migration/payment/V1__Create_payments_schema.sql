-- Create payments schema
CREATE SCHEMA IF NOT EXISTS payments;

-- Create payments table
CREATE TABLE IF NOT EXISTS payments.payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL UNIQUE,
    stripe_payment_id VARCHAR(255) NOT NULL UNIQUE,
    amount NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create payment_events table
CREATE TABLE IF NOT EXISTS payments.payment_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID NOT NULL,
    stripe_event_id VARCHAR(255) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment FOREIGN KEY (payment_id) REFERENCES payments.payments(id)
);

-- Create indexes for performance
CREATE INDEX idx_payments_order_id ON payments.payments(order_id);
CREATE INDEX idx_payments_stripe_payment_id ON payments.payments(stripe_payment_id);
CREATE INDEX idx_payment_events_stripe_event_id ON payments.payment_events(stripe_event_id);
CREATE INDEX idx_payment_events_payment_id ON payments.payment_events(payment_id);
