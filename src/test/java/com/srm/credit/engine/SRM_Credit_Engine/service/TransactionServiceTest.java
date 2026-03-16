package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Receivable;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableTypeRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService Tests")
class TransactionServiceTest {

    @Mock
    private PricingService pricingService;

    @Mock
    private ExchangeRateService exchangeService;

    @Mock
    private ReceivableRepository receivableRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private ReceivableTypeRepository receivableTypeRepo;

    @Mock
    private CurrencyRepository currencyRepo;

    @Mock
    private LoggerObservabilidade loggerObservabilidade;

    @InjectMocks
    private TransactionService transactionService;

    private CreateReceivableRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockRequest = new CreateReceivableRequest();
        mockRequest.setFaceValue(new BigDecimal("10000"));
        mockRequest.setDaysToMaturity(30);
        mockRequest.setReceivableType("DUPLICATA");
        mockRequest.setCurrency("BRL");
        mockRequest.setPaymentCurrency("USD");
    }

    @Test
    @DisplayName("Should execute transaction successfully")
    void testExecuteTransactionSuccessfully() {
        // Arrange
        BigDecimal presentValue = new BigDecimal("9700");
        BigDecimal exchangeRate = new BigDecimal("0.20");
        BigDecimal expectedFinalValue = presentValue.multiply(exchangeRate);

        Receivable mockReceivable = new Receivable();
        mockReceivable.setId(UUID.randomUUID());

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(UUID.randomUUID());
        mockTransaction.setReceivable(mockReceivable);
        mockTransaction.setPresentValue(presentValue);
        mockTransaction.setExchangeRate(exchangeRate);
        mockTransaction.setFinalValue(expectedFinalValue);

        // Mock receivable type
        ReceivableType receivableType = new ReceivableType();
        receivableType.setId(UUID.randomUUID());
        receivableType.setName("DUPLICATA");
        
        // Mock currency
        Currency currency = new Currency();
        currency.setId(UUID.randomUUID());
        currency.setCode("BRL");

        when(pricingService.calculate(
                mockRequest.getFaceValue(),
                mockRequest.getDaysToMaturity(),
                mockRequest.getReceivableType()
        )).thenReturn(presentValue);

        when(exchangeService.getRate(
                mockRequest.getCurrency(),
                mockRequest.getPaymentCurrency()
        )).thenReturn(exchangeRate);

        when(receivableTypeRepo.findByName("DUPLICATA")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = transactionService.execute(mockRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        assertEquals(presentValue, result.getPresentValue());
        assertEquals(exchangeRate, result.getExchangeRate());
        assertEquals(expectedFinalValue, result.getFinalValue());

        verify(pricingService).calculate(
                mockRequest.getFaceValue(),
                mockRequest.getDaysToMaturity(),
                mockRequest.getReceivableType()
        );
        verify(exchangeService).getRate(
                mockRequest.getCurrency(),
                mockRequest.getPaymentCurrency()
        );
        verify(receivableRepo).save(any(Receivable.class));
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should calculate final value correctly")
    void testFinalValueCalculation() {
        // Arrange
        BigDecimal presentValue = new BigDecimal("5000");
        BigDecimal exchangeRate = new BigDecimal("0.25");
        BigDecimal expectedFinalValue = new BigDecimal("1250.00");

        Receivable mockReceivable = new Receivable();
        Transaction mockTransaction = new Transaction();
        mockTransaction.setFinalValue(expectedFinalValue);

        ReceivableType receivableType = new ReceivableType();
        receivableType.setName("DUPLICATA");
        Currency currency = new Currency();
        currency.setCode("BRL");

        when(pricingService.calculate(any(BigDecimal.class), anyInt(), anyString()))
                .thenReturn(presentValue);
        when(exchangeService.getRate(anyString(), anyString())).thenReturn(exchangeRate);
        when(receivableTypeRepo.findByName("DUPLICATA")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = transactionService.execute(mockRequest);

        // Assert
        assertEquals(expectedFinalValue, result.getFinalValue());
    }

    @Test
    @DisplayName("Should set creation timestamp")
    void testTransactionTimestamp() {
        // Arrange
        Receivable mockReceivable = new Receivable();
        Transaction mockTransaction = new Transaction();

        ReceivableType receivableType = new ReceivableType();
        receivableType.setName("DUPLICATA");
        Currency currency = new Currency();
        currency.setCode("BRL");

        when(pricingService.calculate(any(BigDecimal.class), anyInt(), anyString()))
                .thenReturn(new BigDecimal("9700"));
        when(exchangeService.getRate(anyString(), anyString())).thenReturn(new BigDecimal("0.20"));
        when(receivableTypeRepo.findByName("DUPLICATA")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        transactionService.execute(mockRequest);

        // Assert
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo).save(captor.capture());
        assertNotNull(captor.getValue().getCreatedAt());
    }

    @Test
    @DisplayName("Should handle different receivable types")
    void testExecuteWithDifferentReceivableType() {
        // Arrange
        mockRequest.setReceivableType("CHEQUE");

        Receivable mockReceivable = new Receivable();
        Transaction mockTransaction = new Transaction();

        ReceivableType receivableType = new ReceivableType();
        receivableType.setName("CHEQUE");
        Currency currency = new Currency();
        currency.setCode("BRL");

        when(pricingService.calculate(any(BigDecimal.class), anyInt(), eq("CHEQUE")))
                .thenReturn(new BigDecimal("9800"));
        when(exchangeService.getRate(anyString(), anyString())).thenReturn(new BigDecimal("0.20"));
        when(receivableTypeRepo.findByName("CHEQUE")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        transactionService.execute(mockRequest);

        // Assert
        verify(pricingService).calculate(any(BigDecimal.class), anyInt(), eq("CHEQUE"));
    }

    @Test
    @DisplayName("Should link transaction to receivable")
    void testTransactionLinkedToReceivable() {
        // Arrange
        Receivable mockReceivable = new Receivable();
        mockReceivable.setId(UUID.randomUUID());

        Transaction mockTransaction = new Transaction();
        mockTransaction.setReceivable(mockReceivable);

        ReceivableType receivableType = new ReceivableType();
        receivableType.setName("DUPLICATA");
        Currency currency = new Currency();
        currency.setCode("BRL");

        when(pricingService.calculate(any(BigDecimal.class), anyInt(), anyString()))
                .thenReturn(new BigDecimal("9700"));
        when(exchangeService.getRate(anyString(), anyString())).thenReturn(new BigDecimal("0.20"));
        when(receivableTypeRepo.findByName("DUPLICATA")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = transactionService.execute(mockRequest);

        // Assert
        assertNotNull(result.getReceivable());
        assertEquals(mockReceivable.getId(), result.getReceivable().getId());
    }

    @Test
    @DisplayName("Should handle large amounts")
    void testExecuteWithLargeAmount() {
        // Arrange
        mockRequest.setFaceValue(new BigDecimal("1000000"));

        Receivable mockReceivable = new Receivable();
        Transaction mockTransaction = new Transaction();

        ReceivableType receivableType = new ReceivableType();
        receivableType.setName("DUPLICATA");
        Currency currency = new Currency();
        currency.setCode("BRL");

        when(pricingService.calculate(any(BigDecimal.class), anyInt(), anyString()))
                .thenReturn(new BigDecimal("970000"));
        when(exchangeService.getRate(anyString(), anyString())).thenReturn(new BigDecimal("0.20"));
        when(receivableTypeRepo.findByName("DUPLICATA")).thenReturn(Optional.of(receivableType));
        when(currencyRepo.findByCode("BRL")).thenReturn(Optional.of(currency));
        when(receivableRepo.save(any(Receivable.class))).thenReturn(mockReceivable);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Transaction result = transactionService.execute(mockRequest);

        // Assert
        assertNotNull(result);
        verify(transactionRepo).save(any(Transaction.class));
    }
}

