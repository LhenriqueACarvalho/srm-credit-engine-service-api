package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Receivable;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final PricingService pricingService;
    private final ExchangeRateService exchangeService;
    private final ReceivableRepository receivableRepo;
    private final TransactionRepository transactionRepo;

    public TransactionService(
            PricingService pricingService,
            ExchangeRateService exchangeService,
            ReceivableRepository receivableRepo,
            TransactionRepository transactionRepo) {

        this.pricingService = pricingService;
        this.exchangeService = exchangeService;
        this.receivableRepo = receivableRepo;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public Transaction execute(CreateReceivableRequest req) {

        BigDecimal pv = pricingService.calculate(
                req.getFaceValue(),
                req.getDaysToMaturity(),
                req.getReceivableType()
        );

        BigDecimal rate = exchangeService.getRate(
                req.getCurrency(),
                req.getPaymentCurrency()
        );

        BigDecimal finalValue = pv.multiply(rate);

        Receivable receivable = receivableRepo.save(new Receivable());

        Transaction t = new Transaction();

        t.setReceivable(receivable);
        t.setPresentValue(pv);
        t.setExchangeRate(rate);
        t.setFinalValue(finalValue);
        t.setCreatedAt(LocalDateTime.now());

        return transactionRepo.save(t);
    }
}
