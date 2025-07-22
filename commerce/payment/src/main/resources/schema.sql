CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_payment NUMERIC,
    delivery_total NUMERIC,
    fee_total NUMERIC,
    status VARCHAR(15)
);

