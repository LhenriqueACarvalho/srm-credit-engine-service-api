package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CurrencyDTO;
import com.srm.credit.engine.SRM_Credit_Engine.dto.ExchangeRateDTO;
import com.srm.credit.engine.SRM_Credit_Engine.service.CurrencyManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@Tag(name = "Currency Management", description = "APIs para gerenciar moedas e taxas de câmbio")
public class CurrencyController {

    private final CurrencyManagementService service;

    public CurrencyController(CurrencyManagementService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas as moedas")
    public ResponseEntity<List<CurrencyDTO>> listCurrencies() {
        return ResponseEntity.ok(service.listCurrencies());
    }

    @PostMapping
    @Operation(summary = "Criar nova moeda")
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createCurrency(dto));
    }

    @PostMapping("/exchange-rates")
    @Operation(summary = "Registrar nova taxa de câmbio")
    public ResponseEntity<ExchangeRateDTO> createExchangeRate(
            @Valid @RequestBody ExchangeRateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createExchangeRate(dto));
    }

    @GetMapping("/exchange-rates")
    @Operation(summary = "Obter taxa de câmbio")
    public ResponseEntity<ExchangeRateDTO> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(service.getExchangeRate(from, to));
    }

    @PostMapping("/exchange-rates/mock-update")
    @Operation(summary = "Atualizar taxas com integração mockada")
    public ResponseEntity<String> mockExchangeRateUpdate() {
        service.mockExchangeRateUpdate();
        return ResponseEntity.ok("Exchange rates updated from mock service");
    }
}