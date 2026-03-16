package com.srm.credit.engine.SRM_Credit_Engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test to verify that Spring Boot application context loads successfully
 * with test profile and configuration.
 * 
 * This is a minimal smoke test that verifies:
 * - Application can start
 * - Test configuration is valid
 * - No critical bean wiring errors
 */
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
				"spring.cache.type=none",
				"spring.flyway.enabled=false"
		}
)
@ActiveProfiles("test")
@DisplayName("SRM Credit Engine Application Tests")
class SrmCreditEngineApplicationTests {

	@Test
	@DisplayName("Context should load successfully")
	void contextLoads() {
		// Test just verifies that Spring Boot can start without errors
		// with the test profile and configuration
	}

}
