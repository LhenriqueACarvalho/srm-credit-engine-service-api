package com.srm.credit.engine.SRM_Credit_Engine.repository;

import com.srm.credit.engine.SRM_Credit_Engine.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {

    Optional<Currency> findByCode(String code);
}
