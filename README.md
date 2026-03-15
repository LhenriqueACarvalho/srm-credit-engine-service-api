# 📊 SRM Credit Engine - Plataforma de Cessão de Crédito Multimoedas

> **Solução Técnica para Cessão de Crédito com Suporte a Múltiplas Moedas e Estratégias de Precificação**

## 🎯 Visão Geral

O **SRM Credit Engine** é uma aplicação backend robusta desenvolvida em **Java/Spring Boot** que implementa uma plataforma completa para gerenciamento e cessão de créditos em múltiplas moedas. O sistema oferece funcionalidades avançadas de precificação, simulação de recebiveis, gestão de taxas de câmbio e geração de extratos transacionais.

### Características Principais

✅ **Gestão de Recebiveis** - Criação, simulação e análise de recebiveis  
✅ **Suporte Multimoedas** - Gerenciamento de várias moedas com conversão automática  
✅ **Taxas de Câmbio Dinâmicas** - Registro e atualização de taxas em tempo real  
✅ **Simulação de Preços** - Cálculo de preços finais com base em estratégias configuráveis  
✅ **Registro de Transações** - Rastreamento completo de todas as operações  
✅ **Extrato Detalhado** - Geração de extratos consolidados  
✅ **API RESTful Completa** - Documentação Swagger/OpenAPI integrada  
✅ **Validações Robustas** - Camada de validação em toda a aplicação  
✅ **Migrações Automatizadas** - Flyway para versionamento de banco de dados  

---

## 🏗️ Arquitetura

### Stack Tecnológico

| Camada | Tecnologia | Versão |
|--------|-----------|--------|
| **Framework** | Spring Boot | 4.0.3 |
| **Linguagem** | Java | 21 |
| **Banco de Dados** | PostgreSQL | Latest |
| **Build** | Maven | 3.9+ |
| **Migrações** | Flyway | Latest |
| **ORM** | Hibernate/JPA | Latest |
| **Documentação API** | Springdoc-OpenAPI | 3.0.2 |
| **Utilitários** | Lombok | Latest |

### Estrutura do Projeto

```
src/main/
├── java/com/srm/credit/engine/SRM_Credit_Engine/
│   ├── SrmCreditEngineApplication.java     # Classe principal da aplicação
│   ├── config/                              # Configurações do Spring
│   ├── controller/                          # Endpoints REST
│   │   ├── CurrencyController.java         # Gerenciamento de moedas
│   │   ├── ReceivableController.java       # Simulação de recebiveis
│   │   ├── StatementController.java        # Extrato de transações
│   │   └── TransactionController.java      # Registro de transações
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── CreateReceivableRequest.java
│   │   ├── CurrencyDTO.java
│   │   ├── ExchangeRateDTO.java
│   │   ├── SimulationRequestDTO.java
│   │   ├── TransactionStatementDTO.java
│   │   └── response/                        # DTOs de resposta
│   ├── entity/                              # Entidades JPA
│   │   ├── Cedents.java                    # Cedentes de crédito
│   │   ├── Currency.java                   # Moedas
│   │   ├── ExchangeRate.java               # Taxas de câmbio
│   │   ├── Receivable.java                 # Recebiveis
│   │   ├── ReceivableType.java             # Tipos de recebivel
│   │   └── Transaction.java                # Transações
│   ├── exception/                           # Exceções customizadas
│   ├── repository/                          # Interfaces Repository (Spring Data)
│   ├── service/                             # Lógica de negócio
│   │   ├── CurrencyManagementService.java  # Gerenciamento de moedas
│   │   ├── ExchangeRateService.java        # Cálculo de taxas
│   │   ├── PricingService.java             # Precificação
│   │   ├── StatementService.java           # Extratos
│   │   └── TransactionService.java         # Transações
│   ├── strategy/                            # Padrão Strategy para precificação
│   └── validation/                          # Validadores customizados
└── resources/
    ├── application.properties               # Configurações da aplicação
    ├── db/migration/                        # Scripts SQL versionados
    │   ├── V1__Initial_Schema.sql          # Schema inicial
    │   └── V2__Insert_Initial_Data.sql     # Dados iniciais
    ├── static/                              # Arquivos estáticos
    └── templates/                           # Templates Thymeleaf (se aplicável)
```

---

## 📋 Entidades e Modelos de Dados

### 1. **Currency** (Moeda)
Representa as moedas suportadas pelo sistema.

```sql
CREATE TABLE currencies (
    id UUID PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,      -- Ex: USD, BRL, EUR
    name VARCHAR(100) NOT NULL,            -- Ex: Dólar Americano
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `code`: Código ISO 4217 da moeda (ex: USD, BRL, EUR)
- `name`: Nome completo da moeda
- `createdAt`: Data de criação

---

### 2. **ExchangeRate** (Taxa de Câmbio)
Registra as taxas de conversão entre moedas.

```sql
CREATE TABLE exchange_rates (
    id UUID PRIMARY KEY,
    from_currency_id UUID NOT NULL,       -- Moeda de origem
    to_currency_id UUID NOT NULL,         -- Moeda de destino
    rate NUMERIC(19, 4) NOT NULL,         -- Taxa de conversão
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (to_currency_id) REFERENCES currencies(id)
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `fromCurrencyId`: ID da moeda de origem
- `toCurrencyId`: ID da moeda de destino
- `rate`: Taxa de conversão com precisão até 4 casas decimais
- `createdAt`: Data de criação

---

### 3. **ReceivableType** (Tipo de Recebível)
Tipos de recebiveis com seus respectivos spreads.

```sql
CREATE TABLE receivable_types (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,    -- Ex: DUPLICATA, CHEQUE
    spread NUMERIC(5, 4) NOT NULL         -- Spread aplicado
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `name`: Nome do tipo de recebível
- `spread`: Spread (margem) aplicada na precificação

---

### 4. **Cedents** (Cedentes)
Empresas/pessoas que cedem seus créditos.

```sql
CREATE TABLE cedents (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,           -- Nome do cedente
    document VARCHAR(20) NOT NULL UNIQUE  -- CPF/CNPJ
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `name`: Nome completo do cedente
- `document`: CPF ou CNPJ do cedente

---

### 5. **Receivable** (Recebível)
Representa um crédito a ser cedido.

```sql
CREATE TABLE receivables (
    id UUID PRIMARY KEY,
    type_id UUID NOT NULL,                -- Tipo de recebível
    face_value NUMERIC(19, 2) NOT NULL,   -- Valor de face
    currency_id UUID NOT NULL,            -- Moeda
    maturity_date DATE NOT NULL,          -- Data de vencimento
    cedent_id UUID,                       -- Cedente
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (type_id) REFERENCES receivable_types(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (cedent_id) REFERENCES cedents(id)
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `typeId`: Tipo do recebível
- `faceValue`: Valor nominal do crédito
- `currencyId`: Moeda do crédito
- `maturityDate`: Data de vencimento
- `cedentId`: Cedente do crédito
- `createdAt`: Data de criação

---

### 6. **Transaction** (Transação)
Registra todas as operações do sistema.

```sql
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    receivable_id UUID NOT NULL,          -- Recebível associado
    operation_type VARCHAR(50),           -- Tipo de operação
    amount NUMERIC(19, 2),                -- Valor da transação
    currency_id UUID,                     -- Moeda da transação
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receivable_id) REFERENCES receivables(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id)
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `receivableId`: Recebível relacionado
- `operationType`: Tipo de operação realizada
- `amount`: Valor da transação
- `currencyId`: Moeda utilizada
- `createdAt`: Data da transação

---

## 🔌 API RESTful - Endpoints

### Base URL
```
http://localhost:8081/api
```

### Documentação Interativa
A documentação Swagger/OpenAPI está disponível em:
```
http://localhost:8081/api/swagger-ui.html
```

---

### 💱 **Gerenciamento de Moedas** (`/currencies`)

#### 1. Listar Todas as Moedas
```http
GET /currencies
```

**Resposta (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "code": "USD",
    "name": "Dólar Americano"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "code": "BRL",
    "name": "Real Brasileiro"
  }
]
```

---

#### 2. Criar Nova Moeda
```http
POST /currencies
Content-Type: application/json

{
  "code": "EUR",
  "name": "Euro"
}
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "code": "EUR",
  "name": "Euro"
}
```

---

#### 3. Registrar Taxa de Câmbio
```http
POST /currencies/exchange-rates
Content-Type: application/json

{
  "fromCurrency": "USD",
  "toCurrency": "BRL",
  "rate": 4.85
}
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440003",
  "fromCurrency": "USD",
  "toCurrency": "BRL",
  "rate": 4.85
}
```

---

### 📈 **Simulação de Recebiveis** (`/receivables`)

#### Simular Precificação de Recebível
```http
POST /receivables/simulate
Content-Type: application/json

{
  "faceValue": 10000.00,
  "daysToMaturity": 30,
  "receivableType": "DUPLICATA"
}
```

**Resposta (200 OK):**
```json
{
  "simulatedPrice": 9850.50
}
```

**Descrição:**
O sistema calcula o preço de cessão baseado em:
- Valor de face do recebível
- Dias até vencimento (desconto temporal)
- Tipo de recebível (spread específico)
- Taxa de juros configurada

---

### 📋 **Transações** (`/transactions`)

#### 1. Registrar Transação
```http
POST /transactions
Content-Type: application/json

{
  "receivableId": "550e8400-e29b-41d4-a716-446655440000",
  "operationType": "CESSÃO",
  "amount": 9850.50,
  "currencyId": "550e8400-e29b-41d4-a716-446655440001"
}
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440010",
  "receivableId": "550e8400-e29b-41d4-a716-446655440000",
  "operationType": "CESSÃO",
  "amount": 9850.50,
  "currencyId": "550e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2024-03-15T10:30:00"
}
```

---

#### 2. Listar Transações
```http
GET /transactions
```

**Resposta (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440010",
    "receivableId": "550e8400-e29b-41d4-a716-446655440000",
    "operationType": "CESSÃO",
    "amount": 9850.50,
    "currencyId": "550e8400-e29b-41d4-a716-446655440001",
    "createdAt": "2024-03-15T10:30:00"
  }
]
```

---

### 📊 **Extratos** (`/statements`)

#### Gerar Extrato de Transações
```http
GET /statements?fromDate=2024-03-01&toDate=2024-03-31
```

**Resposta (200 OK):**
```json
{
  "period": "2024-03-01 a 2024-03-31",
  "totalTransactions": 5,
  "totalAmount": 50000.00,
  "transactions": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440010",
      "receivableId": "550e8400-e29b-41d4-a716-446655440000",
      "operationType": "CESSÃO",
      "amount": 10000.00,
      "currencyId": "550e8400-e29b-41d4-a716-446655440001",
      "createdAt": "2024-03-15T10:30:00"
    }
  ]
}
```

---

## 🚀 Guia de Instalação e Execução

### Pré-requisitos

- **Java 21+** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/SRM-Credit-Engine.git
cd SRM-Credit-Engine
```

### 2️⃣ Configurar Banco de Dados

**Criar database PostgreSQL:**

```sql
CREATE DATABASE credit_engine;
```

**Criar usuário (opcional):**

```sql
CREATE USER postgres WITH PASSWORD 'password';
ALTER ROLE postgres SET client_encoding TO 'utf8';
ALTER ROLE postgres SET default_transaction_isolation TO 'read committed';
ALTER ROLE postgres SET default_transaction_deferrable TO on;
ALTER ROLE postgres SET timezone TO 'UTC';
```

---

### 3️⃣ Configurar Variáveis de Ambiente

Editar `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/credit_engine
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Server
server.port=8081
server.servlet.context-path=/api
```

---

### 4️⃣ Compilar e Executar

**Compilar o projeto:**

```bash
./mvnw clean install
```

**Executar a aplicação:**

```bash
./mvnw spring-boot:run
```

**Ou usando o JAR gerado:**

```bash
java -jar target/SRM-Credit-Engine-0.0.1-SNAPSHOT.jar
```

---

### 5️⃣ Verificar Execução

A aplicação estará disponível em:

- **API:** `http://localhost:8081/api`
- **Swagger UI:** `http://localhost:8081/api/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8081/api/v3/api-docs`

---

## 🏢 Estrutura de Serviços

### **CurrencyManagementService**
Responsável por gerenciar moedas e taxas de câmbio.

**Métodos Principais:**
- `listCurrencies()` - Lista todas as moedas
- `createCurrency(CurrencyDTO)` - Cria nova moeda
- `saveExchangeRate(ExchangeRateDTO)` - Registra taxa de câmbio
- `getExchangeRate(fromCurrency, toCurrency)` - Obtém taxa entre moedas

---

### **PricingService**
Implementa a lógica de precificação de recebiveis.

**Métodos Principais:**
- `calculate(BigDecimal faceValue, int daysToMaturity, String receivableType)` - Calcula preço final
- Usa padrão Strategy para diferentes tipos de recebiveis

**Fórmula de Cálculo:**
```
Preço Final = Valor de Face × (1 - (Taxa de Desconto + Spread))

Taxa de Desconto = (dias_até_vencimento / 365) × Taxa de Juros
Spread = Spread específico do tipo de recebível
```

---

### **ExchangeRateService**
Gerencia conversões de moedas.

**Métodos Principais:**
- `convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency)` - Converte valores
- `updateExchangeRate(String from, String to, BigDecimal newRate)` - Atualiza taxa

---

### **TransactionService**
Registra e gerencia transações.

**Métodos Principais:**
- `recordTransaction(TransactionRequest)` - Registra nova transação
- `getTransactionsByReceivable(UUID receivableId)` - Lista transações de um recebível
- `getTransactionHistory()` - Histórico completo

---

### **StatementService**
Gera extratos consolidados.

**Métodos Principais:**
- `generateStatement(LocalDate from, LocalDate to)` - Extrato por período
- `getMonthlyStatement(YearMonth)` - Extrato mensal
- `calculateTotals()` - Totalizações

---

## 🔐 Validações

O sistema implementa validações em múltiplas camadas:

### Validações de Entrada (DTOs)
- ✓ Campos obrigatórios não vazios
- ✓ Formatos de dados corretos
- ✓ Ranges numéricos válidos
- ✓ Datas válidas (não no passado)

**Exemplo com Lombok:**
```java
@Valid @RequestBody SimulationRequestDTO req
```

---

### Validações de Negócio
- ✓ Moeda deve existir no sistema
- ✓ Taxa de câmbio deve estar registrada
- ✓ Recebível não pode estar duplicado
- ✓ Cedente deve estar validado

---

## 📊 Migrações de Banco de Dados

O sistema usa **Flyway** para versionamento automático do banco de dados.

### Estrutura de Migrações

```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql        # Schema principal
├── V2__Insert_Initial_Data.sql   # Dados iniciais
└── V3__Add_New_Column.sql        # (Futuras migrações)
```

### Executar Migrações

As migrações são executadas automaticamente na inicialização da aplicação:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

---

## 📚 DTOs Principais

### **SimulationRequestDTO**
```java
public class SimulationRequestDTO {
    @NotNull
    private BigDecimal faceValue;        // Valor de face
    
    @NotNull
    private Integer daysToMaturity;      // Dias até vencimento
    
    @NotBlank
    private String receivableType;       // Tipo de recebível
}
```

### **CurrencyDTO**
```java
public class CurrencyDTO {
    private UUID id;
    
    @NotBlank
    @Size(min=3, max=3)
    private String code;                 // Código ISO
    
    @NotBlank
    private String name;                 // Nome da moeda
}
```

### **ExchangeRateDTO**
```java
public class ExchangeRateDTO {
    private UUID id;
    
    @NotBlank
    private String fromCurrency;         // De qual moeda
    
    @NotBlank
    private String toCurrency;           // Para qual moeda
    
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal rate;             // Taxa de conversão
}
```

### **TransactionStatementDTO**
```java
public class TransactionStatementDTO {
    private UUID id;
    private UUID receivableId;
    private String operationType;
    private BigDecimal amount;
    private UUID currencyId;
    private LocalDateTime createdAt;
}
```

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/com/srm/credit/engine/SRM_Credit_Engine/
└── SrmCreditEngineApplicationTests.java
```

### Executar Testes

```bash
./mvnw test
```

### Com Cobertura

```bash
./mvnw test jacoco:report
```

---

## 🔧 Configurações Avançadas

### Propriedades de Configuração

| Propriedade | Valor Padrão | Descrição |
|-------------|--------------|-----------|
| `server.port` | 8081 | Porta da aplicação |
| `server.servlet.context-path` | /api | Path base da API |
| `spring.jpa.show-sql` | false | Mostrar SQL no console |
| `spring.flyway.enabled` | true | Ativar Flyway |
| `spring.datasource.hikari.maximum-pool-size` | 10 | Conexões do pool |

### Perfis de Execução

**Development:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Production:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## 🐛 Tratamento de Erros

O sistema implementa tratamento robusto de exceções:

### Exceções Customizadas

- `ResourceNotFoundException` - Recurso não encontrado (404)
- `InvalidOperationException` - Operação inválida (400)
- `DuplicateResourceException` - Recurso duplicado (409)
- `ValidationException` - Erro de validação (422)

### Exemplo de Resposta de Erro

```json
{
  "timestamp": "2024-03-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Currency not found with id: abc123",
  "path": "/api/currencies/abc123"
}
```

---

## 📈 Performance e Otimização

### Índices de Banco de Dados
```sql
CREATE INDEX idx_currencies_code ON currencies(code);
CREATE INDEX idx_exchange_rates_from_to ON exchange_rates(from_currency_id, to_currency_id);
CREATE INDEX idx_receivables_currency ON receivables(currency_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
```

### Connection Pooling
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```
---

## 📊 Diagrama de Classes

```
┌─────────────────────┐
│   CurrencyController│
└──────────┬──────────┘
           │
           ▼
┌─────────────────────────────┐
│ CurrencyManagementService   │
└──────────┬──────────────────┘
           │
           ▼
    ┌──────────────┐
    │ Currency     │
    │ ExchangeRate │
    └──────────────┘

┌────────────────────────┐
│ReceivableController    │
└──────────┬─────────────┘
           │
           ▼
┌──────────────────────┐
│ PricingService       │
└──────────┬───────────┘
           │
           ▼
    ┌─────────────┐
    │  Receivable │
    │  Cedents    │
    └─────────────┘

┌─────────────────────┐
│TransactionController│
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ TransactionService  │
└──────────┬──────────┘
           │
           ▼
    ┌────────────┐
    │ Transaction│
    └────────────┘
```

---

## 🚀 Quick Start

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/SRM-Credit-Engine.git
cd SRM-Credit-Engine

# 2. Configure o banco de dados PostgreSQL
createdb credit_engine

# 3. Atualize application.properties com suas credenciais

# 4. Compile e execute
./mvnw clean install
./mvnw spring-boot:run

# 5. Acesse a API
# Swagger: http://localhost:8081/api/swagger-ui.html
```

---

**Última Atualização:** 15 de Março de 2025 
**Versão:** 0.0.1-SNAPSHOT  
**Status:** Em Desenvolvimento

