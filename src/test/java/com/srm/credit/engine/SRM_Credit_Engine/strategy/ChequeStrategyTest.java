package com.srm.credit.engine.SRM_Credit_Engine.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ChequeStrategy Tests")
class ChequeStrategyTest {

    private ChequeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ChequeStrategy();
    }

    @Test
    @DisplayName("Should return spread of 0.025")
    void testGetSpread() {
        // Act
        BigDecimal spread = strategy.getSpread();

        // Assert
        assertNotNull(spread);
        assertEquals(new BigDecimal("0.025"), spread);
    }

    @Test
    @DisplayName("Should return consistent spread on multiple calls")
    void testGetSpreadConsistent() {
        // Act
        BigDecimal spread1 = strategy.getSpread();
        BigDecimal spread2 = strategy.getSpread();

        // Assert
        assertEquals(spread1, spread2);
    }

    @Test
    @DisplayName("Should implement PricingStrategy interface")
    void testImplementsPricingStrategy() {
        // Assert
        assertInstanceOf(PricingStrategy.class, strategy);
    }
}

