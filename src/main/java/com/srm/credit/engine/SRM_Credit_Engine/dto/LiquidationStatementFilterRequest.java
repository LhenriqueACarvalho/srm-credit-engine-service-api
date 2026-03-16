package com.srm.credit.engine.SRM_Credit_Engine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Filtro para consulta de Extrato de Liquidação
 */
@Data
public class LiquidationStatementFilterRequest {

    @NotNull(message = "Data inicial é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "Data final é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private String cedenteName;

    private String currencyCode;

    private Integer pageNumber = 0;

    private Integer pageSize = 100;
}

