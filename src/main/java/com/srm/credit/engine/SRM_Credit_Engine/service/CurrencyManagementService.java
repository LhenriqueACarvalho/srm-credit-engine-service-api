package com.srm.credit.engine.SRM_Credit_Engine.service;

import com.srm.credit.engine.SRM_Credit_Engine.dto.CurrencyDTO;
import com.srm.credit.engine.SRM_Credit_Engine.dto.ExchangeRateDTO;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import com.srm.credit.engine.SRM_Credit_Engine.repository.CurrencyRepository;
import com.srm.credit.engine.SRM_Credit_Engine.repository.ExchangeRateRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CurrencyManagementService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public CurrencyManagementService(
            CurrencyRepository currencyRepository,
            ExchangeRateRepository exchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Cacheable("currencies")
    public List<CurrencyDTO> listCurrencies() {
        return currencyRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "currencies", allEntries = true)
    @Transactional
    public CurrencyDTO createCurrency(CurrencyDTO dto) {
        Currency currency = new Currency();
        currency.setId(UUID.randomUUID());
        currency.setCode(dto.getCode().toUpperCase());
        currency.setName(dto.getName());

        Currency saved = currencyRepository.save(currency);
        return toDTO(saved);
    }

    @Transactional
    public ExchangeRateDTO createExchangeRate(ExchangeRateDTO dto) {
        Currency fromCurrency = currencyRepository.findByCode(dto.getFromCode())
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + dto.getFromCode()));

        Currency toCurrency = currencyRepository.findByCode(dto.getToCode())
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + dto.getToCode()));

        ExchangeRate rate = new ExchangeRate();
        rate.setId(UUID.randomUUID());
        rate.setFromCurrency(fromCurrency);
        rate.setToCurrency(toCurrency);
        rate.setRate(dto.getRate());
        rate.setCreatedAt(LocalDateTime.now());

        ExchangeRate saved = exchangeRateRepository.save(rate);
        return toDTO(saved);
    }

    @Cacheable(value = "exchangeRate", key = "#p0 + '-' + #p1")
    public ExchangeRateDTO getExchangeRate(String from, String to) {
        ExchangeRate rate = exchangeRateRepository
                .findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc(from, to)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exchange rate nao encontrado " + from + "/" + to
                ));
        return toDTO(rate);
    }

    @Transactional
    public void mockExchangeRateUpdate() {
        // Simula integração com serviço externo
        // Em produção, seria chamada uma API real de câmbio
        List<Currency> currencies = currencyRepository.findAll();

        if (currencies.size() >= 2) {
            // Atualiza taxa BRL/USD
            ExchangeRate brlusd = new ExchangeRate();
            brlusd.setId(UUID.randomUUID());
            brlusd.setFromCurrency(currencies.get(0)); // BRL
            brlusd.setToCurrency(currencies.get(1));   // USD
            brlusd.setRate(new BigDecimal("0.2050")); // Mock atualizado
            brlusd.setCreatedAt(LocalDateTime.now());
            exchangeRateRepository.save(brlusd);
        }
    }

    private CurrencyDTO toDTO(Currency currency) {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        return dto;
    }

    private ExchangeRateDTO toDTO(ExchangeRate rate) {
        ExchangeRateDTO dto = new ExchangeRateDTO();
        dto.setFromCode(rate.getFromCurrency().getCode());
        dto.setToCode(rate.getToCurrency().getCode());
        dto.setRate(rate.getRate());
        return dto;
    }
}