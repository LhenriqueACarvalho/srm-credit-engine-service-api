package com.srm.credit.engine.SRM_Credit_Engine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração do Spring MVC para adicionar interceptadores customizados.
 */
@Configuration
public class ConfiguradorWeb implements WebMvcConfigurer {
    
    private final InterceptadorRastreamento interceptadorRastreamento;
    
    public ConfiguradorWeb(InterceptadorRastreamento interceptadorRastreamento) {
        this.interceptadorRastreamento = interceptadorRastreamento;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptadorRastreamento);
    }
}

