package com.srm.credit.engine.SRM_Credit_Engine.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateTransaction() throws Exception {
        String requestBody = """
            {
                "faceValue": 10000,
                "daysToMaturity": 30,
                "receivableType": "duplicata",
                "currency": "BRL",
                "paymentCurrency": "USD"
            }
            """;

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}

