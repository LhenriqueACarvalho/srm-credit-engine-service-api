package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExchangeRateService Tests")
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository repository;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private ExchangeRate mockExchangeRate;
    private Currency mockFromCurrency;
    private Currency mockToCurrency;

    @BeforeEach
    void setUp() {
        mockFromCurrency = new Currency();
        mockFromCurrency.setId(UUID.randomUUID());
        mockFromCurrency.setCode("BRL");
        mockFromCurrency.setName("Brazilian Real");

        mockToCurrency = new Currency();
        mockToCurrency.setId(UUID.randomUUID());
        mockToCurrency.setCode("USD");
        mockToCurrency.setName("US Dollar");

        mockExchangeRate = new ExchangeRate();
        mockExchangeRate.setId(UUID.randomUUID());
        mockExchangeRate.setFromCurrency(mockFromCurrency);
        mockExchangeRate.setToCurrency(mockToCurrency);
        mockExchangeRate.setRate(new BigDecimal("0.20"));
        mockExchangeRate.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return exchange rate for valid currency pair")
    void testGetRateSuccessfully() {
        // Arrange
        when(repository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("BRL", "USD"))
                .thenReturn(Optional.of(mockExchangeRate));

        // Act
        BigDecimal result = exchangeRateService.getRate("BRL", "USD");

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("0.20"), result);
    }

    @Test
    @DisplayName("Should throw exception when exchange rate not found")
    void testGetRateNotFound() {
        // Arrange
        when(repository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("EUR", "JPY"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, 
                () -> exchangeRateService.getRate("EUR", "JPY"));
    }

    @Test
    @DisplayName("Should return most recent exchange rate")
    void testGetRateMostRecent() {
        // Arrange
        ExchangeRate olderRate = new ExchangeRate();
        olderRate.setRate(new BigDecimal("0.19"));
        olderRate.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(repository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("BRL", "USD"))
                .thenReturn(Optional.of(mockExchangeRate));

        // Act
        BigDecimal result = exchangeRateService.getRate("BRL", "USD");

        // Assert
        assertEquals(new BigDecimal("0.20"), result);
    }

    @Test
    @DisplayName("Should handle different currency combinations")
    void testGetRateDifferentCurrencies() {
        // Arrange
        ExchangeRate eurRate = new ExchangeRate();
        eurRate.setRate(new BigDecimal("1.10"));

        when(repository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("EUR", "USD"))
                .thenReturn(Optional.of(eurRate));

        // Act
        BigDecimal result = exchangeRateService.getRate("EUR", "USD");

        // Assert
        assertEquals(new BigDecimal("1.10"), result);
    }

    @Test
    @DisplayName("Should handle precision in exchange rates")
    void testGetRatePrecision() {
        // Arrange
        ExchangeRate preciseRate = new ExchangeRate();
        preciseRate.setRate(new BigDecimal("0.205487"));

        when(repository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("BRL", "USD"))
                .thenReturn(Optional.of(preciseRate));

        // Act
        BigDecimal result = exchangeRateService.getRate("BRL", "USD");

        // Assert
        assertEquals(new BigDecimal("0.205487"), result);
    }
}

