package com.srm.credit.engine.SRM_Credit_Engine.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExchangeRate Entity Tests")
class ExchangeRateTest {

    private ExchangeRate exchangeRate;
    private Currency fromCurrency;
    private Currency toCurrency;

    @BeforeEach
    void setUp() {
        exchangeRate = new ExchangeRate();
        
        fromCurrency = new Currency();
        fromCurrency.setId(UUID.randomUUID());
        fromCurrency.setCode("BRL");
        fromCurrency.setName("Brazilian Real");

        toCurrency = new Currency();
        toCurrency.setId(UUID.randomUUID());
        toCurrency.setCode("USD");
        toCurrency.setName("US Dollar");
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetAndGetId() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        exchangeRate.setId(id);

        // Assert
        assertEquals(id, exchangeRate.getId());
    }

    @Test
    @DisplayName("Should set and get from currency")
    void testSetAndGetFromCurrency() {
        // Act
        exchangeRate.setFromCurrency(fromCurrency);

        // Assert
        assertEquals(fromCurrency, exchangeRate.getFromCurrency());
    }

    @Test
    @DisplayName("Should set and get to currency")
    void testSetAndGetToCurrency() {
        // Act
        exchangeRate.setToCurrency(toCurrency);

        // Assert
        assertEquals(toCurrency, exchangeRate.getToCurrency());
    }

    @Test
    @DisplayName("Should set and get rate")
    void testSetAndGetRate() {
        // Arrange
        BigDecimal rate = new BigDecimal("0.20");

        // Act
        exchangeRate.setRate(rate);

        // Assert
        assertEquals(rate, exchangeRate.getRate());
    }

    @Test
    @DisplayName("Should set and get creation timestamp")
    void testSetAndGetCreatedAt() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        exchangeRate.setCreatedAt(now);

        // Assert
        assertEquals(now, exchangeRate.getCreatedAt());
    }

    @Test
    @DisplayName("Should initialize with all fields")
    void testInitializeAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal rate = new BigDecimal("0.205");
        LocalDateTime now = LocalDateTime.now();

        // Act
        exchangeRate.setId(id);
        exchangeRate.setFromCurrency(fromCurrency);
        exchangeRate.setToCurrency(toCurrency);
        exchangeRate.setRate(rate);
        exchangeRate.setCreatedAt(now);

        // Assert
        assertEquals(id, exchangeRate.getId());
        assertEquals(fromCurrency, exchangeRate.getFromCurrency());
        assertEquals(toCurrency, exchangeRate.getToCurrency());
        assertEquals(rate, exchangeRate.getRate());
        assertEquals(now, exchangeRate.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle precise exchange rates")
    void testPreciseExchangeRate() {
        // Arrange
        BigDecimal preciseRate = new BigDecimal("0.205487123456");

        // Act
        exchangeRate.setRate(preciseRate);

        // Assert
        assertEquals(preciseRate, exchangeRate.getRate());
    }
}

