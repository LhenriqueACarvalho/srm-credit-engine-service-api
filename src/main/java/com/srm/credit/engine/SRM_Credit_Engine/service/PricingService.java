package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.strategy.PricingStrategy;
import com.srm.credit.engine.SRM_Credit_Engine.strategy.PricingStrategyFactory;
import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class PricingService {
    private final PricingStrategyFactory factory;
    private final LoggerObservabilidade loggerObservabilidade;

    private final BigDecimal baseRate = new BigDecimal("0.02");

    public PricingService(PricingStrategyFactory factory, LoggerObservabilidade loggerObservabilidade) {
        this.factory = factory;
        this.loggerObservabilidade = loggerObservabilidade;
    }

    public BigDecimal calculate(BigDecimal faceValue, int days, String type) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        
        try {
            log.debug("[{}] Iniciando cálculo de preço - Valor: {}, Dias: {}, Tipo: {}", 
                    idRequisicao, faceValue, days, type);
            
            PricingStrategy strategy = factory.getStrategy(type);
            log.debug("[{}] Estratégia de preço obtida para tipo: {}", idRequisicao, type);

            BigDecimal spread = strategy.getSpread();
            log.debug("[{}] Spread obtido: {}", idRequisicao, spread);

            BigDecimal months = BigDecimal.valueOf(days)
                    .divide(BigDecimal.valueOf(30), 4, RoundingMode.HALF_UP);
            log.debug("[{}] Meses calculados: {}", idRequisicao, months);

            BigDecimal rate = baseRate.add(spread);
            log.debug("[{}] Taxa total calculada (base + spread): {}", idRequisicao, rate);

            BigDecimal denominator =
                    BigDecimal.ONE.add(rate).pow(months.intValue());
            log.debug("[{}] Denominador calculado: {}", idRequisicao, denominator);

            BigDecimal resultado = faceValue.divide(denominator, 2, RoundingMode.HALF_UP);
            
            log.info("[{}] Cálculo de preço finalizado com sucesso", idRequisicao);
            loggerObservabilidade.registrarCalculoPreco(faceValue, days, type, resultado);
            
            return resultado;
            
        } catch (Exception e) {
            log.error("[{}] Erro ao calcular preço - Valor: {}, Dias: {}, Tipo: {}", 
                    idRequisicao, faceValue, days, type, e);
            throw new RuntimeException("Erro ao calcular preço: " + e.getMessage(), e);
        }
    }
}

