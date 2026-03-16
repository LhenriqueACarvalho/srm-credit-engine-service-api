# 📊 SRM Credit Engine - Plataforma de Cessão de Crédito Multimoedas

> **Solução Técnica para Cessão de Crédito com Suporte a Múltiplas Moedas e Estratégias de Precificação**

## 🎯 Visão Geral

O **SRM Credit Engine** é uma aplicação backend robusta desenvolvida em **Java/Spring Boot** que implementa uma plataforma completa para gerenciamento e cessão de créditos em múltiplas moedas. O sistema oferece funcionalidades avançadas de precificação, simulação de recebiveis, gestão de taxas de câmbio, geração de extratos transacionais e análise analítica de liquidação.

### Características Principais

✅ **Gestão de Recebiveis** - Criação, simulação e análise de recebiveis  
✅ **Suporte Multimoedas** - Gerenciamento de várias moedas com conversão automática  
✅ **Taxas de Câmbio Dinâmicas** - Registro e atualização de taxas em tempo real  
✅ **Simulação de Preços** - Cálculo de preços finais com base em estratégias configuráveis  
✅ **Registro de Transações** - Rastreamento completo de todas as operações  
✅ **Extrato Detalhado** - Geração de extratos consolidados  
✅ **Extrato de Liquidação** - Consultas analíticas paginadas com totalizações por período, cedente e moeda  
✅ **Análise Analítica** - Volumes agregados por moeda e por tipo de recebível  
✅ **Rastreamento Distribuído** - Correlação de logs via headers `X-Request-ID` e `X-User-ID`  
✅ **Observabilidade** - Logging estruturado com AOP, métricas Micrometer e alertas Prometheus  
✅ **Cache Redis** - Cache de consultas frequentes com fallback para `ConcurrentMapCacheManager`  
✅ **Validação de Moeda** - Annotation customizada `@ValidCurrency` para validação via banco  
✅ **Tratamento Global de Erros** - `@RestControllerAdvice` com resposta padronizada  
✅ **API RESTful Completa** - Documentação Swagger/OpenAPI integrada  
✅ **Migrações Automatizadas** - Flyway para versionamento de banco de dados  
✅ **Containerização** - Dockerfile multi-stage e manifesto Kubernetes  

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
| **Cache** | Redis + ConcurrentMapCache | Latest |
| **Métricas** | Micrometer + Prometheus | Latest |
| **Documentação API** | Springdoc-OpenAPI | 3.0.2 |
| **Utilitários** | Lombok | Latest |
| **Containerização** | Docker + Kubernetes | Latest |

### Estrutura do Projeto

```
src/main/
├── java/com/srm/credit/engine/SRM_Credit_Engine/
│   ├── SrmCreditEngineApplication.java          # Classe principal da aplicação
│   ├── config/                                   # Configurações do Spring
│   │   ├── CacheConfiguration.java              # Cache Redis / ConcurrentMap
│   │   ├── ConfiguradorWeb.java                 # Registro do interceptor de rastreamento
│   │   ├── DatabaseHealthIndicator.java         # Health check customizado do BD
│   │   ├── FlywayConfig.java                    # Configuração Flyway
│   │   ├── InterceptadorRastreamento.java       # Interceptor X-Request-ID / X-User-ID
│   │   ├── LoggingAspect.java                   # AOP: logs de entrada/saída/erro
│   │   ├── MetricsConfiguration.java            # Contadores Micrometer customizados
│   │   └── RedisConfig.java                     # Conexão Redis
│   ├── controller/                               # Endpoints REST
│   │   ├── CurrencyController.java              # Gerenciamento de moedas
│   │   ├── LiquidationStatementController.java  # Analytics e extrato de liquidação ★ NOVO
│   │   ├── ReceivableController.java            # Simulação de recebiveis
│   │   ├── StatementController.java             # Extrato de transações
│   │   └── TransactionController.java           # Registro de transações
│   ├── dto/                                      # Data Transfer Objects
│   │   ├── CreateReceivableRequest.java
│   │   ├── CurrencyDTO.java
│   │   ├── ExchangeRateDTO.java
│   │   ├── LiquidationStatementFilterRequest.java  # ★ NOVO
│   │   ├── LiquidationStatementLineItem.java        # ★ NOVO
│   │   ├── LiquidationStatementResponse.java        # ★ NOVO
│   │   ├── SimulationRequestDTO.java
│   │   ├── TransactionStatementDTO.java
│   │   └── response/
│   ├── entity/                                   # Entidades JPA
│   │   ├── Cedents.java
│   │   ├── Currency.java
│   │   ├── ExchangeRate.java
│   │   ├── Receivable.java
│   │   ├── ReceivableType.java
│   │   └── Transaction.java
│   ├── exception/                                # Exceções e tratamento global
│   │   ├── ApiErrorResponse.java                # ★ NOVO - Resposta padronizada de erro
│   │   └── GlobalExceptionHandler.java          # ★ NOVO - @RestControllerAdvice
│   ├── repository/                               # Interfaces Repository (Spring Data)
│   │   ├── CurrencyRepository.java
│   │   ├── ExchangeRateRepository.java
│   │   ├── LiquidationStatementRepository.java  # ★ NOVO - Queries nativas paginadas
│   │   ├── ReceivableRepository.java
│   │   ├── ReceivableTypeRepository.java
│   │   └── TransactionRepository.java
│   ├── service/                                  # Lógica de negócio
│   │   ├── CurrencyManagementService.java
│   │   ├── ExchangeRateService.java
│   │   ├── LiquidationStatementService.java     # ★ NOVO
│   │   ├── PricingService.java
│   │   ├── StatementService.java
│   │   └── TransactionService.java
│   ├── strategy/                                 # Padrão Strategy para precificação
│   │   ├── PricingStrategy.java                 # Interface base
│   │   ├── DuplicataStrategy.java               # Spread: 1,5%
│   │   ├── ChequeStrategy.java                  # Spread: 2,5%
│   │   └── PricingStrategyFactory.java          # Factory de strategies
│   ├── utils/                                    # Utilitários
│   │   ├── ContextoRastreamento.java            # ★ NOVO - ThreadLocal de rastreamento
│   │   └── LoggerObservabilidade.java           # ★ NOVO - Logger estruturado
│   └── validation/                               # Validadores customizados
│       ├── ValidCurrency.java                   # ★ NOVO - Annotation @ValidCurrency
│       └── CurrencyValidator.java               # ★ NOVO - Validator contra BD
└── resources/
    ├── application.properties                    # Configurações da aplicação
    ├── application-prod.properties.example       # Exemplo de configuração produção
    ├── logback-spring.xml                        # Configuração de log estruturado
    ├── db/migration/
    │   ├── V1__Initial_Schema.sql               # Schema inicial
    │   └── V2__Insert_Initial_Data.sql          # Dados iniciais
    └── static/

k8s/
└── deployment.yaml                               # ★ NOVO - Deployment + Service Kubernetes

prometheus/
└── alert-rules.yml                               # ★ NOVO - Regras de alerta Prometheus

scripts/
├── flyway-repair.sql                             # Script de reparo Flyway
└── health-check.sh                              # Script de health check
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
    present_value NUMERIC(19, 2),        -- Valor presente (após desconto)
    exchange_rate NUMERIC(19, 4),        -- Taxa de câmbio aplicada
    final_value NUMERIC(19, 2),          -- Valor final em moeda de pagamento
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receivable_id) REFERENCES receivables(id)
);
```

**Atributos:**
- `id`: Identificador único (UUID)
- `receivableId`: Recebível relacionado
- `presentValue`: Valor presente após aplicação de desconto
- `exchangeRate`: Taxa de câmbio utilizada na transação
- `finalValue`: Valor final da transação na moeda de pagamento
- `createdAt`: Data da transação

---

## 🔌 API RESTful - Endpoints

### Base URL
```
http://localhost:8081/api
```

### Documentação Interativa
```
http://localhost:8081/api/swagger-ui.html
http://localhost:8081/api/v3/api-docs
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

**Fórmula de Cálculo:**
```
Preço Final = Valor de Face × (1 - (Taxa de Desconto + Spread))

Taxa de Desconto = (diasVencimento / 365) × Taxa de Juros
```

| Tipo de Recebível | Spread |
|-------------------|--------|
| `DUPLICATA`       | 1,5%   |
| `CHEQUE`          | 2,5%   |

---

### 📋 **Transações** (`/transactions`)

#### 1. Registrar Transação
```http
POST /transactions
Content-Type: application/json
X-Request-ID: req-001        ← opcional, gerado automaticamente se ausente
X-User-ID: user-123          ← opcional

{
  "receivableType": "DUPLICATA",
  "faceValue": 10000.00,
  "daysToMaturity": 30,
  "currency": "BRL",
  "paymentCurrency": "USD"
}
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440010",
  "receivable": { ... },
  "presentValue": 9700.00,
  "exchangeRate": 0.20,
  "finalValue": 1940.00,
  "createdAt": "2026-03-16T10:30:00"
}
```

> O header `X-Request-ID` é sempre retornado na resposta para correlação de logs.

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

### 🔬 **Analytics e Extrato de Liquidação** (`/analytics`) ★ NOVO

#### Gerar Extrato de Liquidação Paginado
```http
POST /analytics/liquidation-statement
Content-Type: application/json

{
  "startDate": "2026-03-01",
  "endDate": "2026-03-31",
  "cedenteName": "Empresa X",   ← opcional
  "currencyCode": "BRL",        ← opcional
  "pageNumber": 0,
  "pageSize": 100
}
```

**Resposta (200 OK):**
```json
{
  "items": [
    {
      "transactionId": "...",
      "receivableId": "...",
      "cedenteName": "Cedente Sistema",
      "receivableType": "DUPLICATA",
      "faceValue": 10000.00,
      "currency": "BRL",
      "paymentCurrency": "BRL",
      "maturityDate": "2026-04-15",
      "presentValue": 9700.00,
      "exchangeRate": 1.0,
      "finalValue": 9700.00,
      "transactionDate": "2026-03-16T10:30:00"
    }
  ],
  "totalRecords": 50,
  "pageNumber": 0,
  "pageSize": 100,
  "totalPages": 1,
  "totalFaceValue": 500000.00,
  "totalFinalValue": 485000.00,
  "transactionCount": 50
}
```

#### Análise de Volumes por Moeda
```http
GET /analytics/volume-by-currency?startDate=2026-03-01&endDate=2026-03-31
```

**Resposta (200 OK):**
```json
[
  { "currency": "BRL", "totalVolume": 500000.00, "transactionCount": 40 },
  { "currency": "USD", "totalVolume": 150000.00, "transactionCount": 10 }
]
```

#### Análise de Volumes por Tipo de Recebível
```http
GET /analytics/volume-by-receivable-type?startDate=2026-03-01&endDate=2026-03-31
```

**Resposta (200 OK):**
```json
[
  { "receivableType": "DUPLICATA", "totalVolume": 400000.00, "transactionCount": 35 },
  { "receivableType": "CHEQUE",    "totalVolume": 250000.00, "transactionCount": 15 }
]
```

#### Health Check do Analytics
```http
GET /analytics/health
```
```json
{ "status": "UP", "service": "LiquidationStatementService" }
```

---

## 🔍 Rastreamento Distribuído ★ NOVO

Todas as requisições passam pelo `InterceptadorRastreamento`, que:

- Lê o header `X-Request-ID`; se ausente, gera um UUID automaticamente
- Lê o header `X-User-ID` (opcional)
- Armazena ambos em `ContextoRastreamento` via `ThreadLocal`
- Retorna o `X-Request-ID` no header da resposta
- Loga início, fim e tempo decorrido de cada requisição
- Limpa o contexto ao fim de cada ciclo de requisição

```
Request  ──►  InterceptadorRastreamento
                   ├── define X-Request-ID no ThreadLocal
                   ├── define X-User-ID no ThreadLocal (se presente)
                   └── inicia timer

              Controller / Service / Repository
                   └── todos os logs incluem [X-Request-ID]

Response ◄──  InterceptadorRastreamento
                   ├── loga tempo total e status HTTP
                   └── limpa ThreadLocal
```

### Headers suportados

| Header | Direção | Descrição |
|--------|---------|-----------|
| `X-Request-ID` | Entrada/Saída | ID único de correlação de logs |
| `X-User-ID` | Entrada | Identificador do usuário requisitante |

---

## 📡 Observabilidade ★ NOVO

### Logging AOP (`LoggingAspect`)

Intercepta automaticamente via AspectJ:
- **`@Before` controllers** — loga entrada em cada endpoint
- **`@AfterReturning` services** — loga saída com tipo do resultado
- **`@AfterThrowing`** — loga exceções em qualquer camada

### Logger de Observabilidade (`LoggerObservabilidade`)

Utilitário de log estruturado que sempre injeta o `X-Request-ID`:

```java
loggerObservabilidade.inicioOperacao("GerarExtratoLiquidacao", parametros);
loggerObservabilidade.fimOperacao("GerarExtratoLiquidacao", resultado);
loggerObservabilidade.erroOperacao("GerarExtratoLiquidacao", ex, contexto);
loggerObservabilidade.registrarCalculoPreco(valorFace, dias, tipo, resultado);
loggerObservabilidade.registrarOperacaoCambio(origem, destino, taxa, valor);
```

### Métricas Micrometer (`MetricsConfiguration`)

Contadores customizados expostos em `/api/actuator/prometheus`:

| Métrica | Descrição |
|---------|-----------|
| `srm.transactions.total` | Total de transações processadas |
| `srm.receivables.total` | Total de recebiveis criados |
| `srm.exchange_rates.total` | Total de taxas de câmbio atualizadas |

### Health Check de Banco (`DatabaseHealthIndicator`)

Indicador customizado exposto em `/api/actuator/health`:

```json
{
  "status": "UP",
  "components": {
    "database": {
      "status": "UP",
      "details": { "database": "PostgreSQL", "connection": "OK" }
    }
  }
}
```

---

## 🚨 Alertas Prometheus ★ NOVO

Configurado em `prometheus/alert-rules.yml`:

| Alerta | Condição | Janela |
|--------|----------|--------|
| `HighErrorRate` | Taxa de erros > 5% | 5 min |
| `DatabaseConnectionPoolExhausted` | Pool ativo > 90% | 2 min |
| `HighResponseTime` | P95 latência > 1s | 5 min |
| `LowCacheHitRate` | Cache hit < 70% | 10 min |

---

## ⚡ Cache Redis ★ NOVO

Configurado em `CacheConfiguration` com suporte dual:

| `spring.cache.type` | Implementação | Uso |
|---------------------|--------------|-----|
| `redis` | `RedisCacheManager` (TTL 10 min) | Produção |
| `simple` (padrão) | `ConcurrentMapCacheManager` | Desenvolvimento / Testes |

**Caches disponíveis:** `currencies`, `exchangeRate`, `transactions`, `statements`

**Configuração Redis:**
```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000
spring.cache.type=simple
```

---

## ✅ Validação Customizada ★ NOVO

### `@ValidCurrency`

Annotation customizada que valida o código de moeda contra o banco de dados:

```java
@ValidCurrency(mustExist = true, message = "Moeda não encontrada no sistema")
private String currency;

@ValidCurrency(mustExist = true)
private String paymentCurrency;
```

| Parâmetro | Padrão | Descrição |
|-----------|--------|-----------|
| `mustExist` | `false` | Exige que o código exista no banco |
| `message` | `"Invalid currency code"` | Mensagem de erro customizável |

---

## 🛡️ Tratamento Global de Erros ★ NOVO

`GlobalExceptionHandler` centraliza o tratamento de exceções com respostas padronizadas:

| Exceção | Status HTTP | Descrição |
|---------|-------------|-----------|
| `MethodArgumentNotValidException` | 400 | Falha nas validações de entrada com detalhes por campo |
| `IllegalArgumentException` | 400 | Argumento inválido (ex: tipo de recebível inexistente) |
| `Exception` | 500 | Erro interno genérico |

**Exemplo de resposta de erro:**
```json
{
  "timestamp": "2026-03-16T10:30:00",
  "status": 400,
  "error": "Validação Falhou",
  "message": "Parâmetros de entrada inválidos",
  "details": {
    "startDate": "Data inicial é obrigatória",
    "currency": "Moeda não encontrada no sistema"
  },
  "path": "/api/analytics/liquidation-statement"
}
```

---

## 🏢 Estrutura de Serviços

### **CurrencyManagementService**
- `listCurrencies()` — lista com cache `@Cacheable("currencies")`
- `createCurrency(CurrencyDTO)` — cria nova moeda
- `saveExchangeRate(ExchangeRateDTO)` — registra taxa de câmbio
- `getExchangeRate(from, to)` — obtém taxa com cache `@Cacheable("exchangeRate")`

### **PricingService**
- `calculate(faceValue, daysToMaturity, receivableType)` — delega à `PricingStrategyFactory`

### **ExchangeRateService**
- `convertCurrency(amount, from, to)` — converte valores entre moedas

### **TransactionService**
- `execute(CreateReceivableRequest)` — cria recebível, calcula preço e registra transação com rastreamento

### **StatementService**
- `generateStatement(from, to)` — extrato por período

### **LiquidationStatementService** ★ NOVO
- `generateLiquidationStatement(filter)` — extrato paginado com totalizações via SQL nativo otimizado
- `analyzeByVolume(startDate, endDate)` — agrupamento por moeda
- `analyzeByReceivableType(startDate, endDate)` — agrupamento por tipo de recebível

---

## 🎯 Padrão Strategy de Precificação ★ NOVO

```
PricingStrategyFactory
    ├── "duplicata" → DuplicataStrategy (spread = 1,5%)
    └── "cheque"    → ChequeStrategy   (spread = 2,5%)
```

Para adicionar um novo tipo, implemente `PricingStrategy` e registre no `switch` do `PricingStrategyFactory`.

---

## 🐳 Containerização ★ NOVO

### Dockerfile (multi-stage)

```bash
# Build
docker build -t srm-credit-engine:latest .

# Run
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/credit_engine \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  srm-credit-engine:latest
```

### Kubernetes

```bash
# Deploy
kubectl apply -f k8s/deployment.yaml

# Verificar pods
kubectl get pods -l app=srm-credit-engine
```

**Recursos configurados por pod:**

| Recurso | Request | Limit |
|---------|---------|-------|
| CPU | 250m | 500m |
| Memória | 512Mi | 1Gi |

**Probes configuradas:**
- `livenessProbe` → `GET /api/actuator/health` (delay: 30s, period: 10s)
- `readinessProbe` → `GET /api/actuator/health/readiness` (delay: 20s, period: 5s)

O `Deployment` está configurado com **3 réplicas** e exposto via `LoadBalancer` na porta 80 → 8081.

---

## 🚀 Guia de Instalação e Execução

### Pré-requisitos

- **Java 21+**
- **Maven 3.9+**
- **PostgreSQL 12+**
- **Redis** (opcional — fallback automático para cache em memória)

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/SRM-Credit-Engine.git
cd SRM-Credit-Engine
```

### 2️⃣ Configurar Banco de Dados

```sql
CREATE DATABASE credit_engine;
```

### 3️⃣ Configurar `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/credit_engine
spring.datasource.username=postgres
spring.datasource.password=password

spring.flyway.enabled=true
spring.cache.type=simple   # use "redis" em produção
server.port=8081
server.servlet.context-path=/api
```

### 4️⃣ Compilar e Executar

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 5️⃣ Verificar Execução

| Recurso | URL |
|---------|-----|
| API | `http://localhost:8081/api` |
| Swagger UI | `http://localhost:8081/api/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8081/api/v3/api-docs` |
| Actuator Health | `http://localhost:8081/api/actuator/health` |
| Métricas Prometheus | `http://localhost:8081/api/actuator/prometheus` |

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/com/srm/credit/engine/SRM_Credit_Engine/
├── SrmCreditEngineApplicationTests.java          # Smoke test — contexto Spring
├── SrmCreditEngineIntegrationTests.java          # Testes de integração gerais
├── controller/
│   ├── CurrencyControllerTest.java
│   └── TransactionControllerTest.java
├── dto/
│   ├── CreateReceivableRequestTest.java
│   ├── CurrencyDTOTest.java
│   └── ExchangeRateDTOTest.java
├── entity/
│   ├── CurrencyTest.java
│   ├── ExchangeRateTest.java
│   └── TransactionTest.java
├── integration/
│   ├── LiquidationStatementIntegrationTest.java  # ★ NOVO
│   ├── RastreamentoIntegrationTest.java          # ★ NOVO — X-Request-ID
│   └── TransactionIntegrationTest.java
├── service/
│   ├── CurrencyManagementServiceTest.java
│   ├── ExchangeRateServiceTest.java
│   ├── LiquidationStatementServiceTest.java      # ★ NOVO
│   ├── PricingServiceTest.java
│   ├── StatementServiceTest.java
│   └── TransactionServiceTest.java
├── strategy/
│   ├── ChequeStrategyTest.java                   # ★ NOVO
│   ├── DuplicataStrategyTest.java                # ★ NOVO
│   └── PricingStrategyFactoryTest.java           # ★ NOVO
└── utils/
    └── ContextoRastreamentoTest.java             # ★ NOVO
```

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw -Dtest=LiquidationStatementIntegrationTest test
./mvnw -Dtest=RastreamentoIntegrationTest test

# Com cobertura
./mvnw test jacoco:report
```

> **Perfil de teste:** usa H2 em memória + cache `simple` + Flyway desabilitado para isolamento total.

---

## 🔧 Configurações Avançadas

### Propriedades de Configuração

| Propriedade | Valor Padrão | Descrição |
|-------------|--------------|-----------|
| `server.port` | 8081 | Porta da aplicação |
| `server.servlet.context-path` | /api | Path base da API |
| `spring.jpa.show-sql` | false | Mostrar SQL no console |
| `spring.flyway.enabled` | true | Ativar Flyway |
| `spring.cache.type` | simple | Tipo de cache (`simple` ou `redis`) |
| `spring.redis.host` | localhost | Host Redis |
| `spring.redis.port` | 6379 | Porta Redis |
| `spring.datasource.hikari.maximum-pool-size` | 20 | Tamanho máximo do pool |
| `management.endpoints.web.exposure.include` | health,metrics,prometheus | Actuator endpoints |

### Logging Configurado

```properties
logging.level.com.srm.credit.engine=DEBUG
logging.file.name=logs/application.log
logging.max-history=30
logging.max-size=100MB
```

### Perfis de Execução

```bash
# Desenvolvimento
./mvnw spring-boot:run

# Produção
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## 📊 Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────┐
│                         HTTP Request                            │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                  ┌────────────▼────────────┐
                  │  InterceptadorRastreamento│  X-Request-ID / X-User-ID
                  │  + LoggingAspect (AOP)   │  Logs entrada/saída
                  └────────────┬────────────┘
                               │
           ┌───────────────────┼──────────────────────┐
           │                   │                      │
  ┌────────▼──────┐   ┌────────▼──────┐   ┌──────────▼──────────┐
  │CurrencyController│ │TransactionController│ │LiquidationStatement│
  └────────┬──────┘   └────────┬──────┘   │   Controller        │
           │                   │          └──────────┬──────────┘
           ▼                   ▼                     ▼
  ┌─────────────────┐ ┌────────────────┐  ┌──────────────────────┐
  │CurrencyMgmtSvc  │ │TransactionSvc  │  │LiquidationStmtSvc    │
  │@Cacheable cache │ │+PricingService │  │+LoggerObservabilidade│
  └────────┬────────┘ └───────┬────────┘  └──────────┬───────────┘
           │                  │                       │
           └──────────────────┼───────────────────────┘
                              │
                  ┌───────────▼───────────┐
                  │  Spring Data JPA       │
                  │  LiquidationStmtRepo   │
                  │  (SQL Nativo Otimizado)│
                  └───────────┬───────────┘
                              │
                  ┌───────────▼───────────┐
                  │      PostgreSQL        │
                  └───────────────────────┘

           Redis ◄──── CacheConfiguration (TTL 10min)
           Prometheus ◄── MetricsConfiguration + Actuator
```

---

**Última Atualização:** 16 de Março de 2026  
**Versão:** 0.0.1-SNAPSHOT  
**Status:** Em Desenvolvimento

