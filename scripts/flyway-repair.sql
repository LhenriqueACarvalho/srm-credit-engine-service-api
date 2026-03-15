-- Script de Reparação do Flyway
-- Execute este script no PostgreSQL para corrigir o erro de checksum da migração V2
-- Conecte ao banco de dados: credit_engine

-- 1. Verificar o histórico de migrações
SELECT * FROM flyway_schema_history;

-- 2. Deletar o registro de migração com erro (V2)
DELETE FROM flyway_schema_history WHERE version = 2;

-- 3. Re-aplicar a migração V2
-- O Flyway detectará que V2 não está na história e a reaplicará automaticamente

-- Depois de executar este script, reinicie a aplicação Spring Boot
-- A aplicação irá reaplicar a migração V2 com o checksum correto

