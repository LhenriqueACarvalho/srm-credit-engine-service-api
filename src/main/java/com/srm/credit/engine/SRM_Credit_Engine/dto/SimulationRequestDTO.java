package com.srm.credit.engine.SRM_Credit_Engine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SimulationRequestDTO {

    @NotNull
    private String receivableType;

    @NotNull
    @Min(1)
    private BigDecimal faceValue;

    @NotNull
    @Min(1)
    private Integer daysToMaturity;

    @NotNull
    private String currency;

    @NotNull
    private String paymentCurrency;
}
