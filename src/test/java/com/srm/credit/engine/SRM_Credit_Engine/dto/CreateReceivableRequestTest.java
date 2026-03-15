package com.srm.credit.engine.SRM_Credit_Engine.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateReceivableRequest Tests")
class CreateReceivableRequestTest {

    private CreateReceivableRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateReceivableRequest();
    }

    @Test
    @DisplayName("Should set and get face value")
    void testSetAndGetFaceValue() {
        // Act
        BigDecimal value = new BigDecimal("10000");
        request.setFaceValue(value);

        // Assert
        assertEquals(value, request.getFaceValue());
    }

    @Test
    @DisplayName("Should set and get days to maturity")
    void testSetAndGetDaysToMaturity() {
        // Act
        request.setDaysToMaturity(30);

        // Assert
        assertEquals(30, request.getDaysToMaturity());
    }

    @Test
    @DisplayName("Should set and get receivable type")
    void testSetAndGetReceivableType() {
        // Act
        request.setReceivableType("duplicata");

        // Assert
        assertEquals("duplicata", request.getReceivableType());
    }

    @Test
    @DisplayName("Should set and get currency")
    void testSetAndGetCurrency() {
        // Act
        request.setCurrency("BRL");

        // Assert
        assertEquals("BRL", request.getCurrency());
    }

    @Test
    @DisplayName("Should set and get payment currency")
    void testSetAndGetPaymentCurrency() {
        // Act
        request.setPaymentCurrency("USD");

        // Assert
        assertEquals("USD", request.getPaymentCurrency());
    }

    @Test
    @DisplayName("Should set all fields")
    void testSetAllFields() {
        // Act
        request.setFaceValue(new BigDecimal("5000"));
        request.setDaysToMaturity(60);
        request.setReceivableType("cheque");
        request.setCurrency("EUR");
        request.setPaymentCurrency("GBP");

        // Assert
        assertEquals(new BigDecimal("5000"), request.getFaceValue());
        assertEquals(60, request.getDaysToMaturity());
        assertEquals("cheque", request.getReceivableType());
        assertEquals("EUR", request.getCurrency());
        assertEquals("GBP", request.getPaymentCurrency());
    }

    @Test
    @DisplayName("Should handle large amounts")
    void testLargeAmounts() {
        // Act
        BigDecimal largeAmount = new BigDecimal("1000000.50");
        request.setFaceValue(largeAmount);

        // Assert
        assertEquals(largeAmount, request.getFaceValue());
    }

    @Test
    @DisplayName("Should handle long time periods")
    void testLongTimePeriods() {
        // Act
        request.setDaysToMaturity(365);

        // Assert
        assertEquals(365, request.getDaysToMaturity());
    }
}

