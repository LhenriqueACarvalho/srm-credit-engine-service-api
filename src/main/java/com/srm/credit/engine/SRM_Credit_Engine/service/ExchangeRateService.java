package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;
    private final LoggerObservabilidade loggerObservabilidade;

    public ExchangeRateService(ExchangeRateRepository repository, LoggerObservabilidade loggerObservabilidade) {
        this.repository = repository;
        this.loggerObservabilidade = loggerObservabilidade;
    }

    public BigDecimal getRate(String from, String to) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        
        try {
            log.debug("[{}] Buscando taxa de câmbio: {} -> {}", idRequisicao, from, to);
            
            BigDecimal taxa = repository
                    .findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc(from, to)
                    .orElseThrow(() -> {
                        String erro = "Taxa de câmbio não encontrada para: " + from + " -> " + to;
                        log.error("[{}] {}", idRequisicao, erro);
                        return new IllegalArgumentException(erro);
                    })
                    .getRate();
            
            log.info("[{}] Taxa de câmbio obtida com sucesso: {} -> {} = {}", idRequisicao, from, to, taxa);
            loggerObservabilidade.registrarOperacaoCambio(from, to, taxa);
            
            return taxa;
            
        } catch (Exception e) {
            log.error("[{}] Erro ao obter taxa de câmbio: {} -> {}. Detalhes: {}", 
                    idRequisicao, from, to, e.getMessage(), e);
            throw e;
        }
    }
}

