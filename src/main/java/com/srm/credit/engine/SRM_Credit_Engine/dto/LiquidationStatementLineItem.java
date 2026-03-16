package com.srm.credit.engine.SRM_Credit_Engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Registro de transação no Extrato de Liquidação
 */
@Data
@AllArgsConstructor
public class LiquidationStatementLineItem {

    private UUID transactionId;

    private UUID receivableId;

    private String cedenteName;

    private String receivableType;

    private BigDecimal faceValue;

    private String currency;

    private String paymentCurrency;

    private LocalDate maturityDate;

    private BigDecimal presentValue;

    private BigDecimal exchangeRate;

    private BigDecimal finalValue;

    private LocalDateTime transactionDate;
}

