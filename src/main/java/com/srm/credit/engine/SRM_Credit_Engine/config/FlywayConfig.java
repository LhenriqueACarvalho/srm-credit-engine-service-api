package com.srm.credit.engine.SRM_Credit_Engine.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Configuração customizada do Flyway para garantir reparo de migrações com checksum inválido
 * Só ativa em produção quando DataSource está disponível
 */
@Configuration
@Profile("!test")
@ConditionalOnBean(DataSource.class)
public class FlywayConfig {

    @Value("${spring.flyway.enabled:true}")
    private boolean flywayEnabled;

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String flywayLocations;

    /**
     * Bean customizado do Flyway que força o reparo antes de qualquer validação
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true", matchIfMissing = true)
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(flywayLocations)
                .baselineOnMigrate(true)
                .validateOnMigrate(false)
                .load();

        // Executar reparo e migração
        try {
            flyway.repair();
        } catch (Exception e) {
            // Ignorar erros de reparo se já estiver reparado
            System.out.println("Flyway repair attempt completed or skipped: " + e.getMessage());
        }
        
        flyway.migrate();

        return flyway;
    }
}



