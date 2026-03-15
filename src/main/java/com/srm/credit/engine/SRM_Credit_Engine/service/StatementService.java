package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.TransactionStatementDTO;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StatementService {

    private final TransactionRepository transactionRepository;

    public StatementService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

//    public Page<TransactionStatementDTO> getLiquidationStatement(
//            LocalDate startDate,
//            LocalDate endDate,
//            String currency,
//            String cedent,
//            Pageable pageable) {
//
//        // Implementar filtros com SQL nativo otimizado
//        return transactionRepository.findLiquidationStatement(
//                startDate, endDate, currency, cedent, pageable
//        );
//    }
}
