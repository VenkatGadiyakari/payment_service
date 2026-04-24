ALTER TABLE payments.payments ADD COLUMN IF NOT EXISTS razorpay_order_id VARCHAR(255);
ALTER TABLE payments.payments ALTER COLUMN razorpay_payment_id DROP NOT NULL;
CREATE INDEX idx_payments_razorpay_order_id ON payments.payments(razorpay_order_id);
