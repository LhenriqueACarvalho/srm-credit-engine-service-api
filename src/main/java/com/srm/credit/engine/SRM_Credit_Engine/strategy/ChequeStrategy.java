package com.srm.credit.engine.SRM_Credit_Engine.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ChequeStrategy implements PricingStrategy{

    @Override
    public BigDecimal getSpread() {
        return new BigDecimal("0.025");
    }
}
