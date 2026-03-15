# 🤖 Guia de Uso de IA no Projeto

> Documentação de como a IA foi utilizada no desenvolvimento do SRM-Credit-Engine

---

## 📊 Análise Inicial do Projeto

A IA foi utilizada para auxiliar na análise inicial do projeto, onde foram identificados os principais componentes da aplicação. A partir disso, solicitei um plano de execução com as tarefas a serem realizadas. O plano foi dividido em blocos, cada um focado em uma etapa específica do desenvolvimento, como análise de código, implementação de funcionalidades, correções e otimizações, documentação, testes e validação, performance e segurança.

Com isso foi identificado as entidades do banco de dados, e então pude criar os scripts de migração utilizando o Flyway, e também criar os DTOs para as transferências de dados entre as camadas da aplicação. A IA também auxiliou na criação do README.md, onde foram descritas as principais funcionalidades do projeto, a arquitetura utilizada, as tecnologias empregadas, e os endpoints disponíveis na API RESTful.

---

## 🔧 Correções e Ajustes

Na classe de validadores, a IA criou um código onde não estava sendo possível inserir novos registros de moeda, então realizei a correção manualmente para permitir a inserção de novas moedas, e também adicionei uma validação para garantir que as taxas de câmbio sejam positivas.

---

## 🧪 Testes Unitários

Foi solicitado para criar os testes unitários do projeto, acelerando o processo de desenvolvimento e garantindo a qualidade do código. A IA criou testes para os serviços de gerenciamento de moedas, taxas de câmbio, simulação de recebíveis, geração de extratos e transações, garantindo que as funcionalidades estejam funcionando corretamente.

---

## 📝 Descobertas Principais

### 🏗️ Stack Tecnológico

- Java 21
- Spring Boot 4.0.3
- PostgreSQL
- Flyway para migrações
- Springdoc-OpenAPI 3.0.2

### 📦 Componentes Principais

- **Controllers**: 4 (Currency, Receivable, Statement, Transaction)
- **Entidades**: 6 (Currency, ExchangeRate, Cedents, ReceivableType, Receivable, Transaction)
- **Services**: 5 (CurrencyManagement, ExchangeRate, Pricing, Statement, Transaction)
- **DTOs**: Para transferência de dados entre camadas
- **Validações**: Em múltiplas camadas

### 🗄️ Banco de Dados

```
Tabelas:
- currencies
- exchange_rates
- receivable_types
- cedents
- receivables
- transactions

Características:
- Chaves estrangeiras bem definidas
- Índices necessários para performance
```

