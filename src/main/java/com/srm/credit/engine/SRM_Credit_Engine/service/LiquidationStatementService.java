package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementFilterRequest;
import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementLineItem;
import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementResponse;
import com.srm.credit.engine.SRM_Credit_Engine.repository.LiquidationStatementRepository;
import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import com.srm.credit.engine.SRM_Credit_Engine.utils.LoggerObservabilidade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Serviço para consultas analíticas e extrato de liquidação
 * Otimizado para performance em grandes volumes de dados
 */
@Slf4j
@Service
public class LiquidationStatementService {

    private final LiquidationStatementRepository repository;
    private final LoggerObservabilidade loggerObservabilidade;

    public LiquidationStatementService(
            LiquidationStatementRepository repository,
            LoggerObservabilidade loggerObservabilidade
    ) {
        this.repository = repository;
        this.loggerObservabilidade = loggerObservabilidade;
    }

    /**
     * Gera extrato de liquidação com filtros e paginação
     *
     * @param filter Filtros para período, cedente e moeda
     * @return Resposta com dados paginados e totalizações
     */
    @Transactional(readOnly = true)
    public LiquidationStatementResponse generateLiquidationStatement(LiquidationStatementFilterRequest filter) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();

        try {
            log.info("[{}] Iniciando geração de extrato de liquidação. Período: {} a {}, " +
                    "Cedente: {}, Moeda: {}",
                    idRequisicao,
                    filter.getStartDate(),
                    filter.getEndDate(),
                    filter.getCedenteName(),
                    filter.getCurrencyCode()
            );

            loggerObservabilidade.inicioOperacao("GerarExtratoLiquidacao", 
                    Map.of(
                            "startDate", filter.getStartDate().toString(),
                            "endDate", filter.getEndDate().toString(),
                            "cedenteName", filter.getCedenteName() != null ? filter.getCedenteName() : "N/A",
                            "currencyCode", filter.getCurrencyCode() != null ? filter.getCurrencyCode() : "N/A"
                    )
            );

            // Criar paginação
            Pageable pageable = PageRequest.of(
                    filter.getPageNumber(),
                    filter.getPageSize()
            );

            // Executar consulta principal com dados paginados
            log.debug("[{}] Executando consulta paginada de liquidação", idRequisicao);
            Page<Map<String, Object>> pageData = repository.findLiquidationStatement(
                    filter.getStartDate(),
                    filter.getEndDate(),
                    filter.getCedenteName(),
                    filter.getCurrencyCode(),
                    pageable
            );

            // Converter resultados para DTO
            List<LiquidationStatementLineItem> items = convertToLineItems(pageData.getContent());
            log.debug("[{}] Convertidos {} registros para LineItem", idRequisicao, items.size());

            // Executar consulta de totalizações
            log.debug("[{}] Executando consulta de totalizações", idRequisicao);
            Map<String, Object> totals = repository.getLiquidationTotals(
                    filter.getStartDate(),
                    filter.getEndDate(),
                    filter.getCedenteName(),
                    filter.getCurrencyCode()
            );

            // Extrair totalizações com tratamento de nulls
            Object totalFaceValueObj = totals.get("totalFaceValue");
            BigDecimal totalFaceValue = (totalFaceValueObj != null) ? (BigDecimal) totalFaceValueObj : BigDecimal.ZERO;
            
            Object totalFinalValueObj = totals.get("totalFinalValue");
            BigDecimal totalFinalValue = (totalFinalValueObj != null) ? (BigDecimal) totalFinalValueObj : BigDecimal.ZERO;
            
            Long transactionCount = ((Number) totals.getOrDefault("transactionCount", 0L)).longValue();

            log.info("[{}] Extrato gerado com sucesso. Total de {} transações, " +
                    "Valor Final Total: {}",
                    idRequisicao,
                    transactionCount,
                    totalFinalValue
            );

            LiquidationStatementResponse response = new LiquidationStatementResponse(
                    items,
                    pageData.getTotalElements(),
                    filter.getPageNumber(),
                    filter.getPageSize(),
                    pageData.getTotalPages(),
                    totalFaceValue,
                    totalFinalValue,
                    transactionCount
            );

            loggerObservabilidade.fimOperacao("GerarExtratoLiquidacao",
                    Map.of(
                            "totalRecords", response.getTotalRecords(),
                            "totalFinalValue", totalFinalValue.toString(),
                            "transactionCount", transactionCount
                    )
            );

            return response;

        } catch (Exception e) {
            log.error("[{}] Erro ao gerar extrato de liquidação: {}",
                    idRequisicao,
                    e.getMessage(),
                    e
            );
            loggerObservabilidade.erroOperacao("GerarExtratoLiquidacao", e,
                    Map.of(
                            "startDate", filter.getStartDate().toString(),
                            "endDate", filter.getEndDate().toString()
                    )
            );
            throw new RuntimeException("Erro ao gerar extrato de liquidação: " + e.getMessage(), e);
        }
    }

    /**
     * Análise de volumes por moeda no período
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Análise agrupada por moeda
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> analyzeByVolume(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();

        try {
            log.info("[{}] Gerando análise de volumes por moeda. Período: {} a {}",
                    idRequisicao,
                    startDate,
                    endDate
            );

            List<Map<String, Object>> result = repository.getAnalyticsByVolume(startDate, endDate);

            log.info("[{}] Análise concluída com {} grupos de moedas",
                    idRequisicao,
                    result.size()
            );

            return result;

        } catch (Exception e) {
            log.error("[{}] Erro ao analisar volumes: {}", idRequisicao, e.getMessage(), e);
            throw new RuntimeException("Erro ao analisar volumes: " + e.getMessage(), e);
        }
    }

    /**
     * Análise de volumes por tipo de recebível
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Análise agrupada por tipo
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> analyzeByReceivableType(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();

        try {
            log.info("[{}] Gerando análise de volumes por tipo de recebível. Período: {} a {}",
                    idRequisicao,
                    startDate,
                    endDate
            );

            List<Map<String, Object>> result = repository.getAnalyticsByReceivableType(startDate, endDate);

            log.info("[{}] Análise concluída com {} tipos de recebíveis",
                    idRequisicao,
                    result.size()
            );

            return result;

        } catch (Exception e) {
            log.error("[{}] Erro ao analisar tipos: {}", idRequisicao, e.getMessage(), e);
            throw new RuntimeException("Erro ao analisar tipos: " + e.getMessage(), e);
        }
    }

    /**
     * Converte resultados do Map para DTO de LineItem
     *
     * @param dataList Lista de Maps com dados brutos
     * @return Lista de LiquidationStatementLineItem
     */
    private List<LiquidationStatementLineItem> convertToLineItems(List<Map<String, Object>> dataList) {
        List<LiquidationStatementLineItem> items = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            LiquidationStatementLineItem item = new LiquidationStatementLineItem(
                    (java.util.UUID) data.get("transactionId"),
                    (java.util.UUID) data.get("receivableId"),
                    (String) data.get("cedenteName"),
                    (String) data.get("receivableType"),
                    (BigDecimal) data.get("faceValue"),
                    (String) data.get("currency"),
                    (String) data.get("paymentCurrency"),
                    ((java.sql.Date) data.get("maturityDate")).toLocalDate(),
                    (BigDecimal) data.get("presentValue"),
                    (BigDecimal) data.get("exchangeRate"),
                    (BigDecimal) data.get("finalValue"),
                    ((java.sql.Timestamp) data.get("transactionDate")).toLocalDateTime()
            );
            items.add(item);
        }

        return items;
    }
}


