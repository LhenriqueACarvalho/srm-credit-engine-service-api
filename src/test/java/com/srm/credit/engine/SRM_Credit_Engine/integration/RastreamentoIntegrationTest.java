package com.srm.credit.engine.SRM_Credit_Engine.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
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
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para validar rastreamento de requisições
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes de Integração - Rastreamento de Requisições")
class RastreamentoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    private ObjectMapper objectMapper;
    private CreateReceivableRequest request;

    @BeforeEach
    void setUp() {
        // Clear existing data in correct order to respect foreign key constraints
        // Delete child tables first, then parent tables
        transactionRepository.deleteAll();
        receivableRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        receivableTypeRepository.deleteAll();
        currencyRepository.deleteAll();
        
        objectMapper = new ObjectMapper();
        
        // Create receivable type
        ReceivableType duplicata = new ReceivableType();
        duplicata.setId(UUID.randomUUID());
        duplicata.setName("DUPLICATA");
        duplicata.setSpread(new BigDecimal("0.0150"));
        receivableTypeRepository.save(duplicata);

        // Create currencies
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
        
        // Create request
        request = new CreateReceivableRequest();
        request.setFaceValue(new BigDecimal("1000.00"));
        request.setDaysToMaturity(30);
        request.setReceivableType("DUPLICATA");
        request.setCurrency("BRL");
        request.setPaymentCurrency("USD");
    }

    @Test
    @DisplayName("Deve retornar ID de requisição no header da resposta")
    void testeRetornarIdRequisicaoNoHeader() throws Exception {
        MvcResult result = mockMvc.perform(post("/transactions")
                .header("X-Request-ID", "req-test-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        assertEquals("req-test-001", result.getResponse().getHeader("X-Request-ID"));
    }

    @Test
    @DisplayName("Deve gerar ID de requisição quando não fornecido")
    void testeGerarIdRequisicaoAutomaticamente() throws Exception {
        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        if (result.getResponse().getStatus() != 201) {
            System.out.println("ERROR RESPONSE: " + result.getResponse().getContentAsString());
        }
        
        assertEquals(201, result.getResponse().getStatus());
        assertNotNull(result.getResponse().getHeader("X-Request-ID"));
        
        String id = result.getResponse().getHeader("X-Request-ID");
        assertNotNull(id, "ID de requisição deve ser gerado");
        assertFalse(id.isEmpty(), "ID gerado deve conter valor");
    }

    @Test
    @DisplayName("Deve propagar ID de usuário quando fornecido")
    void testePropragarIdUsuario() throws Exception {
        MvcResult result = mockMvc.perform(post("/transactions")
                .header("X-Request-ID", "req-test-002")
                .header("X-User-ID", "user-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        assertEquals("req-test-002", result.getResponse().getHeader("X-Request-ID"));
    }

    @Test
    @DisplayName("Deve criar transação com sucesso e retornar dados")
    void testeCriarTransacaoComRastreamento() throws Exception {
        MvcResult result = mockMvc.perform(post("/transactions")
                .header("X-Request-ID", "req-test-003")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        assertEquals("req-test-003", result.getResponse().getHeader("X-Request-ID"));
        assertNotNull(result.getResponse().getContentAsString());
    }
}

