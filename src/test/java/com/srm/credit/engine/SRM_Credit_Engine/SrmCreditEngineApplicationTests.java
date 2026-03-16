package com.srm.credit.engine.SRM_Credit_Engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.redis.enabled=false",
		"spring.data.redis.host=localhost",
		"spring.data.redis.port=6379"
})
@ActiveProfiles("test")
@DisplayName("SRM Credit Engine Application Tests")
class SrmCreditEngineApplicationTests {

	@Test
	@DisplayName("Context should load successfully")
	void contextLoads() {
	}

}
