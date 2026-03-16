package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementFilterRequest;
import com.srm.credit.engine.SRM_Credit_Engine.dto.LiquidationStatementResponse;
import com.srm.credit.engine.SRM_Credit_Engine.service.LiquidationStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller para consultas analíticas e extratos de liquidação
 */
@Slf4j
@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "APIs de consultas analíticas e extratos de liquidação")
public class LiquidationStatementController {

    private final LiquidationStatementService service;

    public LiquidationStatementController(LiquidationStatementService service) {
        this.service = service;
    }

    /**
     * Gera extrato de liquidação com filtros e paginação
     * Endpoint otimizado para performance em grandes volumes
     *
     * @param filter Filtros: período, cedente, moeda e paginação
     * @return Resposta com dados paginados e totalizações
     */
    @PostMapping("/liquidation-statement")
    @Operation(
            summary = "Gerar Extrato de Liquidação",
            description = "Gera extrato de liquidação com filtros por período, cedente e moeda. " +
                    "Otimizado para performance usando SQL nativo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Extrato gerado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    public ResponseEntity<LiquidationStatementResponse> generateLiquidationStatement(
            @Valid @RequestBody LiquidationStatementFilterRequest filter
    ) {
        log.info("Requisição para gerar extrato de liquidação. Período: {} a {}",
                filter.getStartDate(),
                filter.getEndDate()
        );

        LiquidationStatementResponse response = service.generateLiquidationStatement(filter);

        log.info("Extrato gerado com sucesso. Total de {} transações",
                response.getTransactionCount()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Análise de volumes por moeda no período
     *
     * @param startDate Data inicial (formato: yyyy-MM-dd)
     * @param endDate Data final (formato: yyyy-MM-dd)
     * @return Lista com análise agrupada por moeda
     */
    @GetMapping("/volume-by-currency")
    @Operation(
            summary = "Análise de Volumes por Moeda",
            description = "Retorna análise de volumes agregados por moeda no período especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Análise realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Datas inválidas")
            }
    )
    public ResponseEntity<List<Map<String, Object>>> analyzeByVolume(
            @Parameter(description = "Data inicial no formato yyyy-MM-dd", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "Data final no formato yyyy-MM-dd", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        log.info("Análise de volumes por moeda. Período: {} a {}", startDate, endDate);

        List<Map<String, Object>> result = service.analyzeByVolume(startDate, endDate);

        log.info("Análise concluída com {} grupos de moedas", result.size());

        return ResponseEntity.ok(result);
    }

    /**
     * Análise de volumes por tipo de recebível
     *
     * @param startDate Data inicial (formato: yyyy-MM-dd)
     * @param endDate Data final (formato: yyyy-MM-dd)
     * @return Lista com análise agrupada por tipo de recebível
     */
    @GetMapping("/volume-by-receivable-type")
    @Operation(
            summary = "Análise de Volumes por Tipo de Recebível",
            description = "Retorna análise de volumes agregados por tipo de recebível no período especificado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Análise realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Datas inválidas")
            }
    )
    public ResponseEntity<List<Map<String, Object>>> analyzeByReceivableType(
            @Parameter(description = "Data inicial no formato yyyy-MM-dd", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "Data final no formato yyyy-MM-dd", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        log.info("Análise de volumes por tipo. Período: {} a {}", startDate, endDate);

        List<Map<String, Object>> result = service.analyzeByReceivableType(startDate, endDate);

        log.info("Análise concluída com {} tipos de recebíveis", result.size());

        return ResponseEntity.ok(result);
    }

    /**
     * Health check do serviço de analytics
     *
     * @return Status do serviço
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check do Analytics", description = "Verifica se o serviço de analytics está disponível")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "LiquidationStatementService"));
    }
}


