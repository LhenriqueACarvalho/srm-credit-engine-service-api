package com.srm.credit.engine.SRM_Credit_Engine.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface ValidCurrency {
    String message() default "Invalid currency code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean mustExist() default false;
}
