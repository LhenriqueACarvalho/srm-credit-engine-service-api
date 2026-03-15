package com.srm.credit.engine.SRM_Credit_Engine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateReceivableRequest {

    @NotBlank
    private String receivableType;

    @NotNull
    @Min(1)
    private BigDecimal faceValue;

    @NotNull
    @Min(1)
    private Integer daysToMaturity;

    @NotBlank
    private String currency;

    @NotBlank
    private String paymentCurrency;
}
