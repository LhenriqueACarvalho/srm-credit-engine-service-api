-- Create currencies table
CREATE TABLE currencies (
    id UUID PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create exchange_rates table
CREATE TABLE exchange_rates (
    id UUID PRIMARY KEY,
    from_currency_id UUID NOT NULL,
    to_currency_id UUID NOT NULL,
    rate NUMERIC(19, 4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (to_currency_id) REFERENCES currencies(id)
);

-- Create receivable_types table
CREATE TABLE receivable_types (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    spread NUMERIC(5, 4) NOT NULL
);

-- Create cedents table
CREATE TABLE cedents (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE
);

-- Create receivables table
CREATE TABLE receivables (
    id UUID PRIMARY KEY,
    type_id UUID NOT NULL,
    face_value NUMERIC(19, 2) NOT NULL,
    currency_id UUID NOT NULL,
    maturity_date DATE NOT NULL,
    cedent_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (type_id) REFERENCES receivable_types(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (cedent_id) REFERENCES cedents(id)
);

-- Create transactions table
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    receivable_id UUID NOT NULL,
    present_value NUMERIC(19, 2) NOT NULL,
    exchange_rate NUMERIC(19, 4) NOT NULL,
    final_value NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receivable_id) REFERENCES receivables(id)
);

-- Create indices for performance
CREATE INDEX idx_exchange_rates_currencies ON exchange_rates(from_currency_id, to_currency_id, created_at DESC);
CREATE INDEX idx_receivables_cedent ON receivables(cedent_id);
CREATE INDEX idx_receivables_currency ON receivables(currency_id);
CREATE INDEX idx_receivables_type ON receivables(type_id);
CREATE INDEX idx_receivables_maturity ON receivables(maturity_date);
CREATE INDEX idx_transactions_receivable ON transactions(receivable_id);
CREATE INDEX idx_transactions_created ON transactions(created_at DESC);

