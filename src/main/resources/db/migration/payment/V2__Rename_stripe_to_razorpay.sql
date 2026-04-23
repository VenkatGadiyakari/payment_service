ALTER TABLE payments.payments RENAME COLUMN stripe_payment_id TO razorpay_payment_id;
ALTER TABLE payments.payment_events RENAME COLUMN stripe_event_id TO razorpay_event_id;

DROP INDEX IF EXISTS idx_payments_stripe_payment_id;
DROP INDEX IF EXISTS idx_payment_events_stripe_event_id;

CREATE INDEX idx_payments_razorpay_payment_id ON payments.payments(razorpay_payment_id);
CREATE INDEX idx_payment_events_razorpay_event_id ON payments.payment_events(razorpay_event_id);
