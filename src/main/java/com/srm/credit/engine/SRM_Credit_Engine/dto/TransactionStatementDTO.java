package com.srm.credit.engine.SRM_Credit_Engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatementDTO {

    private UUID transactionId;

    private UUID receivableId;

    private String cedentName;

    private String cedentDocument;

    private String receivableType;

    private BigDecimal faceValue;

    private String currency;

    private BigDecimal presentValue;

    private BigDecimal exchangeRate;

    private String paymentCurrency;

    private BigDecimal finalValue;

    private LocalDate maturityDate;

    private LocalDateTime createdAt;
}

