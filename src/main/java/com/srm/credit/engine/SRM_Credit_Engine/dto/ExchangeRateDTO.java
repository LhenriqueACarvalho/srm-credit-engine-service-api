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

    @NotBlank(message = "Valor nao pode ser nulo ou vazio")
    private String fromCode;

    @NotBlank(message = "Valor nao pode ser nulo ou vazio")
    private String toCode;

    @NotNull(message = "Valor Exchange rate nao pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate precisa ser maior que zero")
    private BigDecimal rate;
}

