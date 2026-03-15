package com.srm.credit.engine.SRM_Credit_Engine.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PricingStrategyFactory Tests")
class PricingStrategyFactoryTest {

    private PricingStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PricingStrategyFactory();
    }

    @Test
    @DisplayName("Should return DuplicataStrategy for 'duplicata' type")
    void testGetDuplicataStrategy() {
        // Act
        PricingStrategy strategy = factory.getStrategy("duplicata");

        // Assert
        assertNotNull(strategy);
        assertInstanceOf(DuplicataStrategy.class, strategy);
    }

    @Test
    @DisplayName("Should return ChequeStrategy for 'cheque' type")
    void testGetChequeStrategy() {
        // Act
        PricingStrategy strategy = factory.getStrategy("cheque");

        // Assert
        assertNotNull(strategy);
        assertInstanceOf(ChequeStrategy.class, strategy);
    }

    @Test
    @DisplayName("Should return DuplicataStrategy for uppercase 'DUPLICATA'")
    void testGetDuplicataStrategyUppercase() {
        // Act
        PricingStrategy strategy = factory.getStrategy("DUPLICATA");

        // Assert
        assertNotNull(strategy);
        assertInstanceOf(DuplicataStrategy.class, strategy);
    }

    @Test
    @DisplayName("Should return ChequeStrategy for uppercase 'CHEQUE'")
    void testGetChequeStrategyUppercase() {
        // Act
        PricingStrategy strategy = factory.getStrategy("CHEQUE");

        // Assert
        assertNotNull(strategy);
        assertInstanceOf(ChequeStrategy.class, strategy);
    }

    @Test
    @DisplayName("Should throw exception for unknown strategy type")
    void testGetStrategyUnknownType() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> factory.getStrategy("unknown"));
    }

    @Test
    @DisplayName("Should throw exception for empty string")
    void testGetStrategyEmptyString() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> factory.getStrategy(""));
    }

    @Test
    @DisplayName("Should throw exception for null string")
    void testGetStrategyNullString() {
        // Act & Assert
        assertThrows(NullPointerException.class, 
                () -> factory.getStrategy(null));
    }

    @Test
    @DisplayName("Should handle mixed case input")
    void testGetStrategyMixedCase() {
        // Act & Assert
        assertDoesNotThrow(() -> factory.getStrategy("DuPlIcAtA"));
        assertDoesNotThrow(() -> factory.getStrategy("ChEqUe"));
    }
}

