package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.service.TransactionService;
import com.srm.credit.engine.SRM_Credit_Engine.utils.ContextoRastreamento;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova transação de recebível")
    public Transaction execute(
            @RequestBody CreateReceivableRequest req
    ) {
        String idRequisicao = ContextoRastreamento.obterIdRequisicao();
        log.info("[{}] Requisição recebida para criar transação: {}", idRequisicao, req);
        
        try {
            Transaction resultado = service.execute(req);
            log.info("[{}] Transação criada com sucesso. ID: {}", idRequisicao, resultado.getId());
            return resultado;
        } catch (Exception e) {
            log.error("[{}] Erro ao criar transação. Detalhes: {}", idRequisicao, e.getMessage(), e);
            throw e;
        }
    }

}
