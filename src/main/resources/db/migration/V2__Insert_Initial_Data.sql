-- Insert currencies
INSERT INTO currencies (id, code, name) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'BRL', 'Brazilian Real'),
    ('550e8400-e29b-41d4-a716-446655440001', 'USD', 'US Dollar')
ON CONFLICT DO NOTHING;

-- Insert receivable types
INSERT INTO receivable_types (id, name, spread) VALUES
    ('550e8400-e29b-41d4-a716-446655440010', 'DUPLICATA', 0.0150),
    ('550e8400-e29b-41d4-a716-446655440011', 'CHEQUE', 0.0250)
ON CONFLICT DO NOTHING;

-- Insert exchange rates
INSERT INTO exchange_rates (id, from_currency_id, to_currency_id, rate, created_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440020',
     '550e8400-e29b-41d4-a716-446655440000',
     '550e8400-e29b-41d4-a716-446655440001',
     0.2000,
     CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440021',
     '550e8400-e29b-41d4-a716-446655440001',
     '550e8400-e29b-41d4-a716-446655440000',
     5.0000,
     CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;