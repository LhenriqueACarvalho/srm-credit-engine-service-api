package com.srm.credit.engine.SRM_Credit_Engine.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction Entity Tests")
class TransactionTest {

    private Transaction transaction;
    private Receivable receivable;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        receivable = new Receivable();
        receivable.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetAndGetId() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        transaction.setId(id);

        // Assert
        assertEquals(id, transaction.getId());
    }

    @Test
    @DisplayName("Should set and get receivable")
    void testSetAndGetReceivable() {
        // Act
        transaction.setReceivable(receivable);

        // Assert
        assertEquals(receivable, transaction.getReceivable());
    }

    @Test
    @DisplayName("Should set and get present value")
    void testSetAndGetPresentValue() {
        // Arrange
        BigDecimal presentValue = new BigDecimal("9700");

        // Act
        transaction.setPresentValue(presentValue);

        // Assert
        assertEquals(presentValue, transaction.getPresentValue());
    }

    @Test
    @DisplayName("Should set and get exchange rate")
    void testSetAndGetExchangeRate() {
        // Arrange
        BigDecimal rate = new BigDecimal("0.20");

        // Act
        transaction.setExchangeRate(rate);

        // Assert
        assertEquals(rate, transaction.getExchangeRate());
    }

    @Test
    @DisplayName("Should set and get final value")
    void testSetAndGetFinalValue() {
        // Arrange
        BigDecimal finalValue = new BigDecimal("1940");

        // Act
        transaction.setFinalValue(finalValue);

        // Assert
        assertEquals(finalValue, transaction.getFinalValue());
    }

    @Test
    @DisplayName("Should set and get creation timestamp")
    void testSetAndGetCreatedAt() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        transaction.setCreatedAt(now);

        // Assert
        assertEquals(now, transaction.getCreatedAt());
    }

    @Test
    @DisplayName("Should initialize with all fields")
    void testInitializeAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal presentValue = new BigDecimal("9700");
        BigDecimal rate = new BigDecimal("0.20");
        BigDecimal finalValue = new BigDecimal("1940");
        LocalDateTime now = LocalDateTime.now();

        // Act
        transaction.setId(id);
        transaction.setReceivable(receivable);
        transaction.setPresentValue(presentValue);
        transaction.setExchangeRate(rate);
        transaction.setFinalValue(finalValue);
        transaction.setCreatedAt(now);

        // Assert
        assertEquals(id, transaction.getId());
        assertEquals(receivable, transaction.getReceivable());
        assertEquals(presentValue, transaction.getPresentValue());
        assertEquals(rate, transaction.getExchangeRate());
        assertEquals(finalValue, transaction.getFinalValue());
        assertEquals(now, transaction.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle calculation of final value from present value and exchange rate")
    void testFinalValueCalculation() {
        // Arrange
        BigDecimal presentValue = new BigDecimal("5000");
        BigDecimal rate = new BigDecimal("0.25");
        BigDecimal expectedFinalValue = presentValue.multiply(rate);

        // Act
        transaction.setPresentValue(presentValue);
        transaction.setExchangeRate(rate);
        transaction.setFinalValue(expectedFinalValue);

        // Assert
        assertEquals(new BigDecimal("1250.00"), transaction.getFinalValue());
    }
}

