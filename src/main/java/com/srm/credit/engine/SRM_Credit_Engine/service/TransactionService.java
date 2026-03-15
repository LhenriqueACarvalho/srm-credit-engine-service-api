package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CreateReceivableRequest;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Receivable;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ReceivableTypeRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final PricingService pricingService;
    private final ExchangeRateService exchangeService;
    private final ReceivableRepository receivableRepo;
    private final TransactionRepository transactionRepo;
    private final ReceivableTypeRepository receivableTypeRepo;
    private final CurrencyRepository currencyRepo;

    public TransactionService(
            PricingService pricingService,
            ExchangeRateService exchangeService,
            ReceivableRepository receivableRepo,
            TransactionRepository transactionRepo,
            ReceivableTypeRepository receivableTypeRepo,
            CurrencyRepository currencyRepo) {

        this.pricingService = pricingService;
        this.exchangeService = exchangeService;
        this.receivableRepo = receivableRepo;
        this.transactionRepo = transactionRepo;
        this.receivableTypeRepo = receivableTypeRepo;
        this.currencyRepo = currencyRepo;
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

        // Buscar tipo de recebível
        ReceivableType receivableType = receivableTypeRepo.findByName(req.getReceivableType().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Receivable type not found: " + req.getReceivableType()));

        // Buscar moeda
        Currency currency = currencyRepo.findByCode(req.getCurrency().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + req.getCurrency()));

        // Criar e preencher receivable com todos os campos obrigatórios
        Receivable receivable = new Receivable();
        receivable.setId(UUID.randomUUID());
        receivable.setType(receivableType);
        receivable.setFaceValue(req.getFaceValue());
        receivable.setCurrency(currency);
        receivable.setMaturityDate(LocalDate.now().plusDays(req.getDaysToMaturity()));
        receivable = receivableRepo.save(receivable);

        // Criar transaction
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID());
        t.setReceivable(receivable);
        t.setPresentValue(pv);
        t.setExchangeRate(rate);
        t.setFinalValue(finalValue);
        t.setCreatedAt(LocalDateTime.now());

        return transactionRepo.save(t);
    }
}


