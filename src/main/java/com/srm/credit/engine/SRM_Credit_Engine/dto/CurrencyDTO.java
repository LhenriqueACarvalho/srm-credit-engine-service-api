package com.srm.credit.engine.SRM_Credit_Engine.dto;

import com.srm.credit.engine.SRM_Credit_Engine.validation.ValidCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {

    @NotBlank(message = "Valor nao pode ser nulo ou vazio")
    @Size(min = 3, max = 3, message = "Valor precisa ter exatamente 3 caracteres")
    @ValidCurrency
    private String code;

    @NotBlank(message = "Valor nao pode ser nulo ou vazio")
    private String name;
}

