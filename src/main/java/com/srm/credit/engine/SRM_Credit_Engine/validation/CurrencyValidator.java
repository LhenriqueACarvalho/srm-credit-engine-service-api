package com.srm.credit.engine.SRM_Credit_Engine.validation;

import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotNull ou @NotBlank vai validar isso
        }

        if (currencyRepository == null) {
            return false;
        }

        return currencyRepository.findByCode(value.toUpperCase()).isPresent();
    }
}

