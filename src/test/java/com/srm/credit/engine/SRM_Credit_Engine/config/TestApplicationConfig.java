package com.srm.credit.engine.SRM_Credit_Engine.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Test configuration for Spring Boot Test context
 * Provides mock beans for test environment
 * 
 * This configuration is loaded automatically via @Profile("test")
 * and provides cache management for test environments without Redis.
 */
@TestConfiguration
@Profile("test")
@SuppressWarnings({"unused", "java:S1118"})
public class TestApplicationConfig {

    /**
     * Provides a simple in-memory cache manager for tests
     * Replaces Redis with local cache
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "currencies",
                "exchangeRates",
                "receivableTypes",
                "transactions",
                "receivables",
                "liquidationStatements"
        );
    }

    /**
     * Mock Redis connection factory for tests
     * Prevents connection attempts to Redis
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        return null; // This will be replaced by the no-op cache manager
    }
}

