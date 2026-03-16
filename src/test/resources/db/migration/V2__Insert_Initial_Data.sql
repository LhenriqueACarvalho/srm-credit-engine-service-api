-- Insert currencies
INSERT INTO currencies (id, code, name)
SELECT '550e8400-e29b-41d4-a716-446655440000', 'BRL', 'Brazilian Real'
WHERE NOT EXISTS (SELECT 1 FROM currencies WHERE code = 'BRL');

INSERT INTO currencies (id, code, name)
SELECT '550e8400-e29b-41d4-a716-446655440001', 'USD', 'US Dollar'
WHERE NOT EXISTS (SELECT 1 FROM currencies WHERE code = 'USD');

-- Insert receivable types
INSERT INTO receivable_types (id, name, spread)
SELECT '550e8400-e29b-41d4-a716-446655440010', 'DUPLICATA', 0.0150
WHERE NOT EXISTS (SELECT 1 FROM receivable_types WHERE name = 'DUPLICATA');

INSERT INTO receivable_types (id, name, spread)
SELECT '550e8400-e29b-41d4-a716-446655440011', 'CHEQUE', 0.0250
WHERE NOT EXISTS (SELECT 1 FROM receivable_types WHERE name = 'CHEQUE');

-- Insert exchange rates
INSERT INTO exchange_rates (id, from_currency_id, to_currency_id, rate, created_at)
SELECT '550e8400-e29b-41d4-a716-446655440020',
       '550e8400-e29b-41d4-a716-446655440000',
       '550e8400-e29b-41d4-a716-446655440001',
       0.2000,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM exchange_rates WHERE id = '550e8400-e29b-41d4-a716-446655440020');

INSERT INTO exchange_rates (id, from_currency_id, to_currency_id, rate, created_at)
SELECT '550e8400-e29b-41d4-a716-446655440021',
       '550e8400-e29b-41d4-a716-446655440001',
       '550e8400-e29b-41d4-a716-446655440000',
       5.0000,
       CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM exchange_rates WHERE id = '550e8400-e29b-41d4-a716-446655440021');


