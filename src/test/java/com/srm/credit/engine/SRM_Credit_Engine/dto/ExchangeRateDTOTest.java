package com.srm.credit.engine.SRM_Credit_Engine.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExchangeRateDTO Tests")
class ExchangeRateDTOTest {

    private ExchangeRateDTO exchangeRateDTO;

    @BeforeEach
    void setUp() {
        exchangeRateDTO = new ExchangeRateDTO();
    }

    @Test
    @DisplayName("Should set and get from code")
    void testSetAndGetFromCode() {
        // Act
        exchangeRateDTO.setFromCode("BRL");

        // Assert
        assertEquals("BRL", exchangeRateDTO.getFromCode());
    }

    @Test
    @DisplayName("Should set and get to code")
    void testSetAndGetToCode() {
        // Act
        exchangeRateDTO.setToCode("USD");

        // Assert
        assertEquals("USD", exchangeRateDTO.getToCode());
    }

    @Test
    @DisplayName("Should set and get rate")
    void testSetAndGetRate() {
        // Act
        BigDecimal rate = new BigDecimal("0.20");
        exchangeRateDTO.setRate(rate);

        // Assert
        assertEquals(rate, exchangeRateDTO.getRate());
    }

    @Test
    @DisplayName("Should set all fields")
    void testSetAllFields() {
        // Act
        exchangeRateDTO.setFromCode("EUR");
        exchangeRateDTO.setToCode("GBP");
        exchangeRateDTO.setRate(new BigDecimal("0.85"));

        // Assert
        assertEquals("EUR", exchangeRateDTO.getFromCode());
        assertEquals("GBP", exchangeRateDTO.getToCode());
        assertEquals(new BigDecimal("0.85"), exchangeRateDTO.getRate());
    }

    @Test
    @DisplayName("Should handle precise decimal values")
    void testPreciseDecimalValues() {
        // Act
        BigDecimal preciseRate = new BigDecimal("0.205487123");
        exchangeRateDTO.setRate(preciseRate);

        // Assert
        assertEquals(preciseRate, exchangeRateDTO.getRate());
    }
}

