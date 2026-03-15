package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CurrencyDTO;
import com.srm.credit.engine.SRM_Credit_Engine.dto.ExchangeRateDTO;
import com.srm.credit.engine.SRM_Credit_Engine.service.CurrencyManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurrencyController Tests")
class CurrencyControllerTest {

    @Mock
    private CurrencyManagementService service;

    @InjectMocks
    private CurrencyController controller;

    @BeforeEach
    void setUp() {
        // Setup executed before each test
    }

    @Test
    @DisplayName("Should list all currencies with status 200")
    void testListCurrencies() {
        // Arrange
        CurrencyDTO brl = new CurrencyDTO();
        brl.setCode("BRL");
        brl.setName("Brazilian Real");

        CurrencyDTO usd = new CurrencyDTO();
        usd.setCode("USD");
        usd.setName("US Dollar");

        List<CurrencyDTO> currencies = Arrays.asList(brl, usd);
        when(service.listCurrencies()).thenReturn(currencies);

        // Act
        ResponseEntity<List<CurrencyDTO>> response = controller.listCurrencies();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service).listCurrencies();
    }

    @Test
    @DisplayName("Should create currency with status 201")
    void testCreateCurrency() {
        // Arrange
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("EUR");
        dto.setName("Euro");

        when(service.createCurrency(any(CurrencyDTO.class))).thenReturn(dto);

        // Act
        ResponseEntity<CurrencyDTO> response = controller.createCurrency(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EUR", response.getBody().getCode());
        verify(service).createCurrency(dto);
    }

    @Test
    @DisplayName("Should create exchange rate with status 201")
    void testCreateExchangeRate() {
        // Arrange
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode("BRL");
        dto.setToCode("USD");
        dto.setRate(new BigDecimal("0.20"));

        when(service.createExchangeRate(any(ExchangeRateDTO.class))).thenReturn(dto);

        // Act
        ResponseEntity<ExchangeRateDTO> response = controller.createExchangeRate(dto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("BRL", response.getBody().getFromCode());
        assertEquals("USD", response.getBody().getToCode());
        verify(service).createExchangeRate(dto);
    }

    @Test
    @DisplayName("Should get exchange rate with status 200")
    void testGetExchangeRate() {
        // Arrange
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode("BRL");
        dto.setToCode("USD");
        dto.setRate(new BigDecimal("0.20"));

        when(service.getExchangeRate("BRL", "USD")).thenReturn(dto);

        // Act
        ResponseEntity<ExchangeRateDTO> response = controller.getExchangeRate("BRL", "USD");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal("0.20"), response.getBody().getRate());
        verify(service).getExchangeRate("BRL", "USD");
    }

    @Test
    @DisplayName("Should call mock exchange rate update")
    void testMockExchangeRateUpdate() {
        // Arrange
        doNothing().when(service).mockExchangeRateUpdate();

        // Act
        ResponseEntity<String> response = controller.mockExchangeRateUpdate();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Exchange rates updated"));
        verify(service).mockExchangeRateUpdate();
    }

    @Test
    @DisplayName("Should handle empty currency list")
    void testListCurrenciesEmpty() {
        // Arrange
        when(service.listCurrencies()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<CurrencyDTO>> response = controller.listCurrencies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}

