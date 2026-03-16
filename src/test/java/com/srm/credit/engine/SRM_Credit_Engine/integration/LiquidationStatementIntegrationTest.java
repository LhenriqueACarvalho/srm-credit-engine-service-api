package com.srm.credit.engine.SRM_Credit_Engine.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementFilterRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Receivable;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableTypeRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {
				"spring.redis.enabled=false",
				"spring.data.redis.host=localhost",
				"spring.data.redis.port=6379"
		})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("LiquidationStatementController Integration Tests")
class LiquidationStatementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReceivableTypeRepository receivableTypeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ReceivableRepository receivableRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private ReceivableType receivableType;
    private Currency currency;

    @BeforeEach
    void setUp() {
        // Clean up data
        transactionRepository.deleteAll();
        receivableRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        receivableTypeRepository.deleteAll();
        currencyRepository.deleteAll();

        // Create test data
        receivableType = new ReceivableType();
        receivableType.setId(UUID.randomUUID());
        receivableType.setName("DUPLICATA");
        receivableType.setSpread(new BigDecimal("0.015"));
        receivableTypeRepository.save(receivableType);

        currency = new Currency();
        currency.setId(UUID.randomUUID());
        currency.setCode("BRL");
        currency.setName("Brazilian Real");
        currencyRepository.save(currency);

        // Create sample transactions
        createSampleTransactions();
    }

    private void createSampleTransactions() {
        for (int i = 0; i < 5; i++) {
            Receivable receivable = new Receivable();
            receivable.setId(UUID.randomUUID());
            receivable.setType(receivableType);
            receivable.setFaceValue(new BigDecimal("10000"));
            receivable.setCurrency(currency);
            receivable.setMaturityDate(LocalDate.now().plusDays(30));
            receivableRepository.save(receivable);

            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID());
            transaction.setReceivable(receivable);
            transaction.setPresentValue(new BigDecimal("9700"));
            transaction.setExchangeRate(new BigDecimal("1.0"));
            transaction.setFinalValue(new BigDecimal("9700"));
            transaction.setCreatedAt(LocalDateTime.now().minusDays(i));
            transactionRepository.save(transaction);
        }
    }

    @Test
    @DisplayName("Should generate liquidation statement successfully")
    void testGenerateLiquidationStatement() throws Exception {
        LiquidationStatementFilterRequest filter = new LiquidationStatementFilterRequest();
        filter.setStartDate(LocalDate.now().minusDays(30));
        filter.setEndDate(LocalDate.now().plusDays(1));
        filter.setCurrencyCode("BRL");
        filter.setPageNumber(0);
        filter.setPageSize(10);

        mockMvc.perform(post("/analytics/liquidation-statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords").value(5))
                .andExpect(jsonPath("$.transactionCount").value(5))
                .andExpect(jsonPath("$.items.length()").value(5))
                .andExpect(jsonPath("$.totalFaceValue").value(50000));
    }

    @Test
    @DisplayName("Should filter liquidation statement by currency")
    void testLiquidationStatementByCurrency() throws Exception {
        LiquidationStatementFilterRequest filter = new LiquidationStatementFilterRequest();
        filter.setStartDate(LocalDate.now().minusDays(30));
        filter.setEndDate(LocalDate.now().plusDays(1));
        filter.setCurrencyCode("BRL");
        filter.setPageNumber(0);
        filter.setPageSize(10);

        mockMvc.perform(post("/analytics/liquidation-statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionCount").value(5));
    }

    @Test
    @DisplayName("Should analyze volume by currency")
    void testAnalyzeVolumeByCurrency() throws Exception {
        mockMvc.perform(get("/analytics/volume-by-currency")
                .param("startDate", LocalDate.now().minusDays(30).toString())
                .param("endDate", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should analyze volume by receivable type")
    void testAnalyzeVolumeByReceivableType() throws Exception {
        mockMvc.perform(get("/analytics/volume-by-receivable-type")
                .param("startDate", LocalDate.now().minusDays(30).toString())
                .param("endDate", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return health status")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/analytics/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void testPagination() throws Exception {
        LiquidationStatementFilterRequest filter = new LiquidationStatementFilterRequest();
        filter.setStartDate(LocalDate.now().minusDays(30));
        filter.setEndDate(LocalDate.now().plusDays(1));
        filter.setPageNumber(0);
        filter.setPageSize(2);

        mockMvc.perform(post("/analytics/liquidation-statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(3));
    }

    @Test
    @DisplayName("Should validate date range")
    void testValidateDateRange() throws Exception {
        LiquidationStatementFilterRequest filter = new LiquidationStatementFilterRequest();
        filter.setStartDate(null);
        filter.setEndDate(LocalDate.now());

        mockMvc.perform(post("/analytics/liquidation-statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest());
    }
}


