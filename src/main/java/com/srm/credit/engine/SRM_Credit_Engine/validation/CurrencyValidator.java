package com.srm.credit.engine.SRM_Credit_Engine.validation;

import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Autowired
    private CurrencyRepository currencyRepository;

    private ValidCurrency annotation;

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        if (currencyRepository == null) {
            return false;
        }

        boolean exists = currencyRepository.findByCode(value.toUpperCase()).isPresent();


        return !annotation.mustExist() || exists;
    }
}

