package com.srm.credit.engine.SRM_Credit_Engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.srm.credit.engine")
@EnableJpaRepositories(basePackages = "com.srm.credit.engine.SRM_Credit_Engine.repository")
@EnableCaching
public class SrmCreditEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrmCreditEngineApplication.class, args);
	}

}
