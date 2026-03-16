package com.srm.credit.engine.SRM_Credit_Engine.integration;

import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableTypeRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ReceivableTypeRepository receivableTypeRepository;

    @Autowired
    private ReceivableRepository receivableRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        // Clear existing data in correct order to respect foreign key constraints
        // Delete child tables first, then parent tables
        transactionRepository.deleteAll();
        receivableRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        receivableTypeRepository.deleteAll();
        currencyRepository.deleteAll();
        // Create receivable types
        ReceivableType duplicata = new ReceivableType();
        duplicata.setId(UUID.randomUUID());
        duplicata.setName("DUPLICATA");
        duplicata.setSpread(new BigDecimal("0.0150"));
        receivableTypeRepository.save(duplicata);

        // ...existing code...
        Currency brl = new Currency();
        brl.setId(UUID.randomUUID());
        brl.setCode("BRL");
        brl.setName("Brazilian Real");
        currencyRepository.save(brl);

        Currency usd = new Currency();
        usd.setId(UUID.randomUUID());
        usd.setCode("USD");
        usd.setName("US Dollar");
        currencyRepository.save(usd);

        // Create exchange rate
        ExchangeRate rate = new ExchangeRate();
        rate.setId(UUID.randomUUID());
        rate.setFromCurrency(brl);
        rate.setToCurrency(usd);
        rate.setRate(new BigDecimal("0.20"));
        rate.setCreatedAt(LocalDateTime.now());
        exchangeRateRepository.save(rate);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        String requestBody = """
            {
                "faceValue": 10000,
                "daysToMaturity": 30,
                "receivableType": "DUPLICATA",
                "currency": "BRL",
                "paymentCurrency": "USD"
            }
            """;

        mockMvc.perform(post("/transactions")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}

