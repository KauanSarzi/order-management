-- =============================================================
-- Order Management System — Script de inicialização do banco
-- Amazon RDS PostgreSQL 16
-- =============================================================

CREATE TABLE IF NOT EXISTS customers (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(50),
    address    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS orders (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id  UUID NOT NULL REFERENCES customers(id),
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING','PROCESSING','SHIPPED','DELIVERED','CANCELLED')),
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0.00,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_items (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_name VARCHAR(255) NOT NULL,
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    subtotal     NUMERIC(12,2) NOT NULL
);

-- Índices para buscas frequentes
CREATE INDEX IF NOT EXISTS idx_orders_customer    ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_status      ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_items_order  ON order_items(order_id);

-- Dados de exemplo para demonstração
INSERT INTO customers (name, email, phone, address) VALUES
    ('João Silva',    'joao@example.com',   '(11) 99999-0001', 'Rua das Flores, 100 — São Paulo/SP'),
    ('Maria Souza',   'maria@example.com',  '(21) 99999-0002', 'Av. Atlântica, 200 — Rio de Janeiro/RJ'),
    ('Carlos Lima',   'carlos@example.com', '(31) 99999-0003', 'Rua Central, 300 — Belo Horizonte/MG')
ON CONFLICT (email) DO NOTHING;
