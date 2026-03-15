package com.srm.credit.engine.SRM_Credit_Engine.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Currency Entity Tests")
class CurrencyTest {

    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = new Currency();
    }

    @Test
    @DisplayName("Should set and get id")
    void testSetAndGetId() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        currency.setId(id);

        // Assert
        assertEquals(id, currency.getId());
    }

    @Test
    @DisplayName("Should set and get code")
    void testSetAndGetCode() {
        // Act
        currency.setCode("USD");

        // Assert
        assertEquals("USD", currency.getCode());
    }

    @Test
    @DisplayName("Should set and get name")
    void testSetAndGetName() {
        // Act
        currency.setName("US Dollar");

        // Assert
        assertEquals("US Dollar", currency.getName());
    }

    @Test
    @DisplayName("Should initialize with all fields")
    void testInitializeAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String code = "EUR";
        String name = "Euro";

        // Act
        currency.setId(id);
        currency.setCode(code);
        currency.setName(name);

        // Assert
        assertEquals(id, currency.getId());
        assertEquals(code, currency.getCode());
        assertEquals(name, currency.getName());
    }
}

