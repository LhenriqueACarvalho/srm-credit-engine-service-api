package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementFilterRequest;
import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementResponse;
import com.srm.credit.engine.SRM_Credit_Engine.repository.LiquidationStatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LiquidationStatementService Tests")
class LiquidationStatementServiceTest {

    @Mock
    private LiquidationStatementRepository repository;

    @Mock
    private com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade loggerObservabilidade;

    @InjectMocks
    private LiquidationStatementService service;

    private LiquidationStatementFilterRequest filter;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 31);

        filter = new LiquidationStatementFilterRequest();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setCedenteName(null);
        filter.setCurrencyCode("BRL");
        filter.setPageNumber(0);
        filter.setPageSize(10);
    }

    @Test
    @DisplayName("Should generate liquidation statement with valid filter")
    void testGenerateLiquidationStatement() {
        // Arrange
        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("transactionId", UUID.randomUUID());
        lineItem.put("receivableId", UUID.randomUUID());
        lineItem.put("cedenteName", "Test Cedente");
        lineItem.put("receivableType", "DUPLICATA");
        lineItem.put("faceValue", new BigDecimal("10000"));
        lineItem.put("currency", "BRL");
        lineItem.put("paymentCurrency", "BRL");
        lineItem.put("maturityDate", java.sql.Date.valueOf(LocalDate.of(2024, 2, 1)));
        lineItem.put("presentValue", new BigDecimal("9700"));
        lineItem.put("exchangeRate", new BigDecimal("1.0"));
        lineItem.put("finalValue", new BigDecimal("9700"));
        lineItem.put("transactionDate", java.sql.Timestamp.valueOf(LocalDateTime.now()));

        Page<Map<String, Object>> page = new PageImpl<>(
                Collections.singletonList(lineItem),
                PageRequest.of(0, 10),
                1
        );

        Map<String, Object> totals = new HashMap<>();
        totals.put("totalFaceValue", new BigDecimal("10000"));
        totals.put("totalFinalValue", new BigDecimal("9700"));
        totals.put("transactionCount", 1L);

        // Mock with specific argument matchers
        when(repository.findLiquidationStatement(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL"),
                any()
        )).thenReturn(page);

        when(repository.getLiquidationTotals(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL")
        )).thenReturn(totals);

        // Act
        LiquidationStatementResponse response = service.generateLiquidationStatement(filter);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalRecords());
        assertEquals(1, response.getItems().size());
        assertEquals(new BigDecimal("10000"), response.getTotalFaceValue());
        assertEquals(new BigDecimal("9700"), response.getTotalFinalValue());
        assertEquals(1L, response.getTransactionCount());
    }

    @Test
    @DisplayName("Should return empty result when no transactions found")
    void testGenerateLiquidationStatementEmpty() {
        // Arrange
        Page<Map<String, Object>> emptyPage = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(0, 10),
                0
        );

        Map<String, Object> totals = new HashMap<>();
        totals.put("totalFaceValue", null);
        totals.put("totalFinalValue", null);
        totals.put("transactionCount", 0L);

        when(repository.findLiquidationStatement(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL"),
                any()
        )).thenReturn(emptyPage);

        when(repository.getLiquidationTotals(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL")
        )).thenReturn(totals);

        // Act
        LiquidationStatementResponse response = service.generateLiquidationStatement(filter);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getTotalRecords());
        assertTrue(response.getItems().isEmpty());
        assertEquals(0L, response.getTransactionCount());
    }

    @Test
    @DisplayName("Should analyze volumes by currency")
    void testAnalyzeByVolume() {
        // Arrange
        Map<String, Object> currencyAnalysis = new HashMap<>();
        currencyAnalysis.put("currency", "BRL");
        currencyAnalysis.put("transactionCount", 5L);
        currencyAnalysis.put("totalFaceValue", new BigDecimal("50000"));
        currencyAnalysis.put("totalFinalValue", new BigDecimal("48500"));
        currencyAnalysis.put("avgExchangeRate", new BigDecimal("1.0"));

        when(repository.getAnalyticsByVolume(eq(startDate), eq(endDate)))
                .thenReturn(Collections.singletonList(currencyAnalysis));

        // Act
        List<Map<String, Object>> result = service.analyzeByVolume(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BRL", result.getFirst().get("currency"));
        assertEquals(5L, result.getFirst().get("transactionCount"));
    }

    @Test
    @DisplayName("Should analyze volumes by receivable type")
    void testAnalyzeByReceivableType() {
        // Arrange
        Map<String, Object> typeAnalysis = new HashMap<>();
        typeAnalysis.put("receivableType", "DUPLICATA");
        typeAnalysis.put("transactionCount", 3L);
        typeAnalysis.put("totalFaceValue", new BigDecimal("30000"));
        typeAnalysis.put("totalFinalValue", new BigDecimal("29100"));
        typeAnalysis.put("avgSpread", new BigDecimal("0.015"));

        when(repository.getAnalyticsByReceivableType(eq(startDate), eq(endDate)))
                .thenReturn(Collections.singletonList(typeAnalysis));

        // Act
        List<Map<String, Object>> result = service.analyzeByReceivableType(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DUPLICATA", result.getFirst().get("receivableType"));
        assertEquals(3L, result.getFirst().get("transactionCount"));
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void testGenerateLiquidationStatementWithPagination() {
        // Arrange
        filter.setPageNumber(1);
        filter.setPageSize(20);

        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("transactionId", UUID.randomUUID());
        lineItem.put("receivableId", UUID.randomUUID());
        lineItem.put("cedenteName", "Test Cedente");
        lineItem.put("receivableType", "CHEQUE");
        lineItem.put("faceValue", new BigDecimal("5000"));
        lineItem.put("currency", "USD");
        lineItem.put("paymentCurrency", "USD");
        lineItem.put("maturityDate", java.sql.Date.valueOf(LocalDate.of(2024, 2, 15)));
        lineItem.put("presentValue", new BigDecimal("4900"));
        lineItem.put("exchangeRate", new BigDecimal("0.20"));
        lineItem.put("finalValue", new BigDecimal("980"));
        lineItem.put("transactionDate", java.sql.Timestamp.valueOf(LocalDateTime.now()));

        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> item = new HashMap<>(lineItem);
            item.put("transactionId", UUID.randomUUID());
            item.put("receivableId", UUID.randomUUID());
            items.add(item);
        }

        Page<Map<String, Object>> page = new PageImpl<>(
                items,
                PageRequest.of(1, 20),
                25
        );

        Map<String, Object> totals = new HashMap<>();
        totals.put("totalFaceValue", new BigDecimal("100000"));
        totals.put("totalFinalValue", new BigDecimal("19600"));
        totals.put("transactionCount", 20L);

        when(repository.findLiquidationStatement(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL"),
                any()
        )).thenReturn(page);

        when(repository.getLiquidationTotals(
                eq(startDate),
                eq(endDate),
                isNull(),
                eq("BRL")
        )).thenReturn(totals);

        // Act
        LiquidationStatementResponse response = service.generateLiquidationStatement(filter);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getPageNumber());
        assertEquals(20, response.getPageSize());
        assertEquals(25, response.getTotalRecords());
        assertEquals(2, response.getTotalPages());
        assertEquals(20L, response.getTransactionCount());
    }
}
