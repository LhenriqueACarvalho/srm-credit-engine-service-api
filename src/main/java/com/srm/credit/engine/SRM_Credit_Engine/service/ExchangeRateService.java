package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;

    public ExchangeRateService(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    public BigDecimal getRate(String from, String to) {

        return repository
                .findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc(from, to)
                .orElseThrow()
                .getRate();
    }
}
