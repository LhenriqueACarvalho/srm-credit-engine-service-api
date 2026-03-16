package com.srm.credit.engine.SRM_Credit_Engine.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Desabilita auto-configuração do Redis quando tipo de cache não é redis
 * Evita erros de conexão quando Redis não está disponível
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = false)
public class RedisConfig {
    // Redis será autoconfigurado apenas quando spring.cache.type=redis
}

