package com.srm.credit.engine.SRM_Credit_Engine.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDTO {

    @NotBlank(message = "Source currency code cannot be blank")
    private String fromCode;

    @NotBlank(message = "Target currency code cannot be blank")
    private String toCode;

    @NotNull(message = "Exchange rate cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be greater than 0")
    private BigDecimal rate;
}

