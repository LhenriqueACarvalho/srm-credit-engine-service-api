package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatementService Tests")
class StatementServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private StatementService statementService;

    @BeforeEach
    void setUp() {
        // Setup executed before each test
    }

    @Test
    @DisplayName("Should initialize StatementService successfully")
    void testStatementServiceInitialization() {
        // Assert
        assertNotNull(statementService);
        assertNotNull(transactionRepository);
    }

    @Test
    @DisplayName("Should have TransactionRepository injected")
    void testTransactionRepositoryInjection() {
        // Assert
        assertNotNull(transactionRepository);
    }
}

