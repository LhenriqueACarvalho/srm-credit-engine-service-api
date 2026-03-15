package com.srm.credit.engine.SRM_Credit_Engine.strategy;

import org.springframework.stereotype.Component;

@Component
public class PricingStrategyFactory {
    public PricingStrategy getStrategy(String type) {

        return switch (type.toLowerCase()) {

            case "duplicata" -> new DuplicataStrategy();
            case "cheque" -> new ChequeStrategy();

            default -> throw new IllegalArgumentException("Tipo Invalido: " + type);
        };
    }
}
