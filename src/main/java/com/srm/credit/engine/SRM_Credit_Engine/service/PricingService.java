package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.strategy.PricingStrategy;
import com.srm.credit.engine.SRM_Credit_Engine.strategy.PricingStrategyFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {
    private final PricingStrategyFactory factory;

    private final BigDecimal baseRate = new BigDecimal("0.02");

    public PricingService(PricingStrategyFactory factory) {
        this.factory = factory;
    }

    public BigDecimal calculate(BigDecimal faceValue, int days, String type) {

        PricingStrategy strategy = factory.getStrategy(type);

        BigDecimal spread = strategy.getSpread();

        BigDecimal months = BigDecimal.valueOf(days)
                .divide(BigDecimal.valueOf(30), 4, RoundingMode.HALF_UP);

        BigDecimal rate = baseRate.add(spread);

        BigDecimal denominator =
                BigDecimal.ONE.add(rate).pow(months.intValue());

        return faceValue.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
