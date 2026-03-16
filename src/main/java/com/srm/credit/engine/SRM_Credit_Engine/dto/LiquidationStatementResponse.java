package com.srm.credit.engine.SRM_Credit_Engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resposta do Extrato de Liquidação com paginação e totalizações
 */
@Data
@AllArgsConstructor
public class LiquidationStatementResponse {

    private List<LiquidationStatementLineItem> items;

    private long totalRecords;

    private int pageNumber;

    private int pageSize;

    private long totalPages;

    private BigDecimal totalFaceValue;

    private BigDecimal totalFinalValue;

    private long transactionCount;
}

