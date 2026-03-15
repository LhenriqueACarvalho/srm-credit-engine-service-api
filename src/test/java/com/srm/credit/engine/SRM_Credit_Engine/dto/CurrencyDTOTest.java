package com.srm.credit.engine.SRM_Credit_Engine.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CurrencyDTO Tests")
class CurrencyDTOTest {

    private CurrencyDTO currencyDTO;

    @BeforeEach
    void setUp() {
        currencyDTO = new CurrencyDTO();
    }

    @Test
    @DisplayName("Should set and get code")
    void testSetAndGetCode() {
        // Act
        currencyDTO.setCode("USD");

        // Assert
        assertEquals("USD", currencyDTO.getCode());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetAndGetName() {
        // Act
        currencyDTO.setName("US Dollar");

        // Assert
        assertEquals("US Dollar", currencyDTO.getName());
    }

    @Test
    @DisplayName("Should set both code and name")
    void testSetBothFields() {
        // Act
        currencyDTO.setCode("EUR");
        currencyDTO.setName("Euro");

        // Assert
        assertEquals("EUR", currencyDTO.getCode());
        assertEquals("Euro", currencyDTO.getName());
    }

    @Test
    @DisplayName("Should allow null values")
    void testNullValues() {
        // Act & Assert
        currencyDTO.setCode(null);
        currencyDTO.setName(null);
        assertNull(currencyDTO.getCode());
        assertNull(currencyDTO.getName());
    }
}

