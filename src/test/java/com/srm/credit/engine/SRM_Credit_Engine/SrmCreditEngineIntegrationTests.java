package com.srm.credit.engine.SRM_Credit_Engine;

import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import com.srm.credit.engine.SRM_Credit_Engine.service.CurrencyManagementService;
import com.srm.credit.engine.SRM_Credit_Engine.service.ExchangeRateService;
import com.srm.credit.engine.SRM_Credit_Engine.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SRM Credit Engine Integration Tests")
class SrmCreditEngineIntegrationTests {

    @Autowired(required = false)
    private CurrencyRepository currencyRepository;

    @Autowired(required = false)
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired(required = false)
    private CurrencyManagementService currencyManagementService;

    @Autowired(required = false)
    private ExchangeRateService exchangeRateService;

    @Autowired(required = false)
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        // Clean up before each test if repositories are available
        if (currencyRepository != null && exchangeRateRepository != null) {
            exchangeRateRepository.deleteAll();
            currencyRepository.deleteAll();
        }
    }

    @Test
    void contextLoads() {
        // Verifica se o contexto da aplicação carrega corretamente
        assertNotNull(currencyManagementService);
        assertNotNull(exchangeRateService);
        assertNotNull(pricingService);
    }

    @Test
    @DisplayName("Should load all services")
    void testServicesLoaded() {
        // Assert
        assertNotNull(currencyManagementService);
        assertNotNull(exchangeRateService);
        assertNotNull(pricingService);
    }

    @Test
    @DisplayName("Should load all repositories")
    void testRepositoriesLoaded() {
        // Assert
        assertNotNull(currencyRepository);
        assertNotNull(exchangeRateRepository);
    }
}

