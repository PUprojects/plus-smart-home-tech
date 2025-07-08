CREATE TABLE IF NOT EXISTS carts (
    shopping_cart_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR,
    UNIQUE(username)
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID,
    product_id UUID,
    quantity INTEGER
);