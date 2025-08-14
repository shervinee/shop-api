CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- USERS
CREATE TABLE IF NOT EXISTS users (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email           TEXT NOT NULL UNIQUE,
  password_hash   TEXT NOT NULL,
  roles           TEXT NOT NULL DEFAULT 'USER',
  created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- PRODUCTS
CREATE TABLE IF NOT EXISTS products (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sku         TEXT NOT NULL UNIQUE,
  name        TEXT NOT NULL,
  description TEXT,
  price       NUMERIC(10,2) NOT NULL CHECK (price >= 0),
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ORDERS
CREATE TABLE IF NOT EXISTS orders (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id    UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  status     TEXT NOT NULL DEFAULT 'NEW' CHECK (status IN ('NEW','PAID','SHIPPED','CANCELLED')),
  total      NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (total >= 0),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ORDER ITEMS
CREATE TABLE IF NOT EXISTS order_items (
   id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   order_id    UUID NOT NULL REFERENCES orders(id)   ON DELETE CASCADE,
   product_id  UUID NOT NULL REFERENCES products(id) ON DELETE RESTRICT,
   quantity    INT  NOT NULL CHECK (quantity > 0),
   unit_price  NUMERIC(10,2) NOT NULL CHECK (unit_price >= 0),
   CONSTRAINT uq_order_item UNIQUE (order_id, product_id)
);

-- Helpful indexes
CREATE INDEX IF NOT EXISTS idx_orders_user_id          ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id    ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product_id  ON order_items(product_id);
