package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CurrencyDTO;
import com.srm.credit.engine.SRM_Credit_Engine.dto.ExchangeRateDTO;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyManagementService Tests")
class CurrencyManagementServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private CurrencyManagementService currencyManagementService;

    private Currency mockCurrencyBRL;
    private Currency mockCurrencyUSD;

    @BeforeEach
    void setUp() {
        mockCurrencyBRL = new Currency();
        mockCurrencyBRL.setId(UUID.randomUUID());
        mockCurrencyBRL.setCode("BRL");
        mockCurrencyBRL.setName("Brazilian Real");

        mockCurrencyUSD = new Currency();
        mockCurrencyUSD.setId(UUID.randomUUID());
        mockCurrencyUSD.setCode("USD");
        mockCurrencyUSD.setName("US Dollar");
    }

    @Test
    @DisplayName("Should list all currencies")
    void testListCurrencies() {
        // Arrange
        List<Currency> currencies = Arrays.asList(mockCurrencyBRL, mockCurrencyUSD);
        when(currencyRepository.findAll()).thenReturn(currencies);

        // Act
        List<CurrencyDTO> result = currencyManagementService.listCurrencies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BRL", result.get(0).getCode());
        assertEquals("USD", result.get(1).getCode());
    }

    @Test
    @DisplayName("Should return empty list when no currencies exist")
    void testListCurrenciesEmpty() {
        // Arrange
        when(currencyRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<CurrencyDTO> result = currencyManagementService.listCurrencies();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should create a new currency")
    void testCreateCurrency() {
        // Arrange
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("eur");
        dto.setName("Euro");

        when(currencyRepository.save(any(Currency.class))).thenReturn(mockCurrencyBRL);

        // Act
        CurrencyDTO result = currencyManagementService.createCurrency(dto);

        // Assert
        assertNotNull(result);
        assertEquals("BRL", result.getCode());
        assertEquals("Brazilian Real", result.getName());

        // Verify save was called
        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        verify(currencyRepository).save(captor.capture());
        assertEquals("EUR", captor.getValue().getCode());
    }

    @Test
    @DisplayName("Should create exchange rate successfully")
    void testCreateExchangeRate() {
        // Arrange
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode("BRL");
        dto.setToCode("USD");
        dto.setRate(new BigDecimal("0.20"));

        when(currencyRepository.findByCode("BRL")).thenReturn(Optional.of(mockCurrencyBRL));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(mockCurrencyUSD));

        ExchangeRate savedRate = new ExchangeRate();
        savedRate.setId(UUID.randomUUID());
        savedRate.setFromCurrency(mockCurrencyBRL);
        savedRate.setToCurrency(mockCurrencyUSD);
        savedRate.setRate(new BigDecimal("0.20"));
        savedRate.setCreatedAt(LocalDateTime.now());

        when(exchangeRateRepository.save(any(ExchangeRate.class))).thenReturn(savedRate);

        // Act
        ExchangeRateDTO result = currencyManagementService.createExchangeRate(dto);

        // Assert
        assertNotNull(result);
        assertEquals("BRL", result.getFromCode());
        assertEquals("USD", result.getToCode());
        assertEquals(new BigDecimal("0.20"), result.getRate());
    }

    @Test
    @DisplayName("Should throw exception when creating exchange rate with non-existent source currency")
    void testCreateExchangeRateSourceNotFound() {
        // Arrange
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode("XXX");
        dto.setToCode("USD");
        dto.setRate(new BigDecimal("0.20"));

        when(currencyRepository.findByCode("XXX")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> currencyManagementService.createExchangeRate(dto));
    }

    @Test
    @DisplayName("Should throw exception when creating exchange rate with non-existent target currency")
    void testCreateExchangeRateTargetNotFound() {
        // Arrange
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode("BRL");
        dto.setToCode("XXX");
        dto.setRate(new BigDecimal("0.20"));

        when(currencyRepository.findByCode("BRL")).thenReturn(Optional.of(mockCurrencyBRL));
        when(currencyRepository.findByCode("XXX")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> currencyManagementService.createExchangeRate(dto));
    }

    @Test
    @DisplayName("Should get exchange rate successfully")
    void testGetExchangeRate() {
        // Arrange
        ExchangeRate rate = new ExchangeRate();
        rate.setId(UUID.randomUUID());
        rate.setFromCurrency(mockCurrencyBRL);
        rate.setToCurrency(mockCurrencyUSD);
        rate.setRate(new BigDecimal("0.20"));
        rate.setCreatedAt(LocalDateTime.now());

        when(exchangeRateRepository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("BRL", "USD"))
                .thenReturn(Optional.of(rate));

        // Act
        ExchangeRateDTO result = currencyManagementService.getExchangeRate("BRL", "USD");

        // Assert
        assertNotNull(result);
        assertEquals("BRL", result.getFromCode());
        assertEquals("USD", result.getToCode());
    }

    @Test
    @DisplayName("Should throw exception when exchange rate not found")
    void testGetExchangeRateNotFound() {
        // Arrange
        when(exchangeRateRepository.findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc("EUR", "JPY"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> currencyManagementService.getExchangeRate("EUR", "JPY"));
    }

    @Test
    @DisplayName("Should mock exchange rate update")
    void testMockExchangeRateUpdate() {
        // Arrange
        List<Currency> currencies = Arrays.asList(mockCurrencyBRL, mockCurrencyUSD);
        when(currencyRepository.findAll()).thenReturn(currencies);
        when(exchangeRateRepository.save(any(ExchangeRate.class))).thenReturn(new ExchangeRate());

        // Act
        currencyManagementService.mockExchangeRateUpdate();

        // Assert
        verify(exchangeRateRepository).save(any(ExchangeRate.class));
    }

    @Test
    @DisplayName("Should uppercase currency code when creating")
    void testCreateCurrencyUppercase() {
        // Arrange
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("gbp");
        dto.setName("British Pound");

        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> {
            Currency arg = invocation.getArgument(0);
            return arg;
        });

        // Act
        currencyManagementService.createCurrency(dto);

        // Assert
        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        verify(currencyRepository).save(captor.capture());
        assertEquals("GBP", captor.getValue().getCode());
    }
}

