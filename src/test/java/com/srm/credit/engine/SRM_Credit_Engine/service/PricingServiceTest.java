package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.strategy.ChequeStrategy;
import com.srm.credit.engine.SRM_Credit_Engine.strategy.DuplicataStrategy;
import com.srm.credit.engine.SRM_Credit_Engine.strategy.PricingStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PricingService Tests")
class PricingServiceTest {

    @Mock
    private PricingStrategyFactory factory;

    @InjectMocks
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        // Setup executed before each test
    }

    @Test
    @DisplayName("Should calculate present value with Duplicata strategy")
    void testCalculatePresentValueWithDuplicataStrategy() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("10000");
        int days = 30;
        String type = "duplicata";

        when(factory.getStrategy("duplicata")).thenReturn(new DuplicataStrategy());

        // Act
        BigDecimal result = pricingService.calculate(faceValue, days, type);

        // Assert
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.compareTo(faceValue) < 0);
    }

    @Test
    @DisplayName("Should calculate present value with Cheque strategy")
    void testCalculatePresentValueWithChequeStrategy() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("5000");
        int days = 60;
        String type = "cheque";

        when(factory.getStrategy("cheque")).thenReturn(new ChequeStrategy());

        // Act
        BigDecimal result = pricingService.calculate(faceValue, days, type);

        // Assert
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.compareTo(faceValue) < 0);
    }

    @Test
    @DisplayName("Should return lower present value for Cheque than Duplicata")
    void testChequeStrategyReturnsLowerValueThanDuplicata() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("10000");
        int days = 30;

        when(factory.getStrategy("duplicata")).thenReturn(new DuplicataStrategy());
        when(factory.getStrategy("cheque")).thenReturn(new ChequeStrategy());

        // Act
        BigDecimal duplicataResult = pricingService.calculate(faceValue, days, "duplicata");
        BigDecimal chequeResult = pricingService.calculate(faceValue, days, "cheque");

        // Assert
        assertTrue(chequeResult.compareTo(duplicataResult) < 0);
    }

    @Test
    @DisplayName("Should calculate different values for different time periods")
    void testCalculateDifferentValueForDifferentTimePeriods() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("10000");
        when(factory.getStrategy("duplicata")).thenReturn(new DuplicataStrategy());

        // Act
        BigDecimal result30Days = pricingService.calculate(faceValue, 30, "duplicata");
        BigDecimal result60Days = pricingService.calculate(faceValue, 60, "duplicata");

        // Assert
        assertNotEquals(result30Days, result60Days);
        assertTrue(result60Days.compareTo(result30Days) < 0);
    }

    @Test
    @DisplayName("Should throw exception for invalid receivable type")
    void testCalculateWithInvalidType() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("10000");
        int days = 30;
        String invalidType = "invalid_type";

        when(factory.getStrategy("invalid_type"))
                .thenThrow(new IllegalArgumentException("Tipo Invalido: invalid_type"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> pricingService.calculate(faceValue, days, invalidType));
    }

    @Test
    @DisplayName("Should handle zero days")
    void testCalculateWithZeroDays() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("10000");
        when(factory.getStrategy("duplicata")).thenReturn(new DuplicataStrategy());

        // Act
        BigDecimal result = pricingService.calculate(faceValue, 0, "duplicata");

        // Assert
        assertNotNull(result);
        // With 0 days, the result should be approximately the face value divided by (1 + rate)^0 = 1
        assertEquals(faceValue.setScale(2, java.math.RoundingMode.HALF_UP), 
                result.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("Should handle large amounts")
    void testCalculateWithLargeAmount() {
        // Arrange
        BigDecimal faceValue = new BigDecimal("1000000");
        int days = 90;
        when(factory.getStrategy("duplicata")).thenReturn(new DuplicataStrategy());

        // Act
        BigDecimal result = pricingService.calculate(faceValue, days, "duplicata");

        // Assert
        assertNotNull(result);
        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.compareTo(faceValue) < 0);
    }
}

