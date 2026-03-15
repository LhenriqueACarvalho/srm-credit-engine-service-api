package com.srm.credit.engine.SRM_Credit_Engine.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsConfiguration {

    private final Counter transactionCounter;
    private final Counter receivableCounter;
    private final Counter exchangeRateCounter;

    public MetricsConfiguration(MeterRegistry meterRegistry) {
        this.transactionCounter = Counter.builder("srm.transactions.total")
                .description("Total de transações processadas")
                .register(meterRegistry);

        this.receivableCounter = Counter.builder("srm.receivables.total")
                .description("Total de recebiveis criados")
                .register(meterRegistry);

        this.exchangeRateCounter = Counter.builder("srm.exchange_rates.total")
                .description("Total de taxas de câmbio atualizadas")
                .register(meterRegistry);
    }

    public void incrementTransactionCounter() {
        transactionCounter.increment();
    }

    public void incrementReceivableCounter() {
        receivableCounter.increment();
    }

    public void incrementExchangeRateCounter() {
        exchangeRateCounter.increment();
    }
}