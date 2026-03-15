package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionController Tests")
class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    private CreateReceivableRequest mockRequest;
    private Transaction mockTransaction;

    @BeforeEach
    void setUp() {
        mockRequest = new CreateReceivableRequest();
        mockRequest.setFaceValue(new BigDecimal("10000"));
        mockRequest.setDaysToMaturity(30);
        mockRequest.setReceivableType("duplicata");
        mockRequest.setCurrency("BRL");
        mockRequest.setPaymentCurrency("USD");

        mockTransaction = new Transaction();
        mockTransaction.setId(UUID.randomUUID());
        mockTransaction.setPresentValue(new BigDecimal("9700"));
        mockTransaction.setExchangeRate(new BigDecimal("0.20"));
        mockTransaction.setFinalValue(new BigDecimal("1940"));
        mockTransaction.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should execute transaction successfully")
    void testExecuteTransaction() {
        // Arrange
        when(service.execute(any(CreateReceivableRequest.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = controller.execute(mockRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        assertEquals(new BigDecimal("9700"), result.getPresentValue());
        assertEquals(new BigDecimal("0.20"), result.getExchangeRate());
        assertEquals(new BigDecimal("1940"), result.getFinalValue());
        verify(service).execute(mockRequest);
    }

    @Test
    @DisplayName("Should return transaction with timestamp")
    void testExecuteTransactionTimestamp() {
        // Arrange
        when(service.execute(any(CreateReceivableRequest.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = controller.execute(mockRequest);

        // Assert
        assertNotNull(result.getCreatedAt());
    }

    @Test
    @DisplayName("Should pass request data to service")
    void testExecutePassesRequestToService() {
        // Arrange
        when(service.execute(any(CreateReceivableRequest.class))).thenReturn(mockTransaction);

        // Act
        controller.execute(mockRequest);

        // Assert
        verify(service).execute(mockRequest);
    }

    @Test
    @DisplayName("Should handle different receivable types")
    void testExecuteWithDifferentTypes() {
        // Arrange
        mockRequest.setReceivableType("cheque");
        when(service.execute(any(CreateReceivableRequest.class))).thenReturn(mockTransaction);

        // Act
        controller.execute(mockRequest);

        // Assert
        verify(service).execute(mockRequest);
    }
}

