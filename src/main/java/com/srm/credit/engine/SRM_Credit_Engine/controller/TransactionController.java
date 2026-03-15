package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova transação de recebível")
    public Transaction execute(
            @RequestBody CreateReceivableRequest req
    ) {
        return service.execute(req);
    }

}
