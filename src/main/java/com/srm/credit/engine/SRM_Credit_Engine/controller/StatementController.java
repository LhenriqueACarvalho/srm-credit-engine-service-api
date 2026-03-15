package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.TransactionStatementDTO;
import com.srm.credit.engine.SRM_Credit_Engine.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statements")
@Tag(name = "Statement", description = "APIs para consultas analíticas e extratos")
public class StatementController {

    private final StatementService service;

    public StatementController(StatementService service) {
        this.service = service;
    }

//    @GetMapping("/liquidation")
//    @Operation(summary = "Extrato de liquidações com filtros")
//    public ResponseEntity<Page<TransactionStatementDTO>> getLiquidationStatement(
//            @RequestParam(required = false) LocalDate startDate,
//            @RequestParam(required = false) LocalDate endDate,
//            @RequestParam(required = false) String currency,
//            @RequestParam(required = false) String cedent,
//            Pageable pageable) {
//
//        Page<TransactionStatementDTO> result = service.getLiquidationStatement(
//                startDate, endDate, currency, cedent, pageable
//        );
//        return ResponseEntity.ok(result);
//    }
}