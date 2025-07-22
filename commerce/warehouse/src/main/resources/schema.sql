CREATE TABLE IF NOT EXISTS warehouse (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    quantity INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    delivery_id UUID DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID REFERENCES orders(order_id),
    product_id UUID,
    quantity INTEGER,
    PRIMARY KEY(order_id, product_id)
);