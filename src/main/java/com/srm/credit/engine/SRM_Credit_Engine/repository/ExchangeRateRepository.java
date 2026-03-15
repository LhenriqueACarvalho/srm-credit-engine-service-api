package com.srm.credit.engine.SRM_Credit_Engine.repository;

import com.srm.credit.engine.SRM_Credit_Engine.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, UUID> {
    Optional<ExchangeRate> findTopByFromCurrency_CodeAndToCurrency_CodeOrderByCreatedAtDesc(
            String from,
            String to
    );
}
