package com.srm.credit.engine.SRM_Credit_Engine.repository;

import com.srm.credit.engine.SRM_Credit_Engine.entity.ReceivableType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReceivableTypeRepository extends JpaRepository<ReceivableType, UUID> {
    Optional<ReceivableType> findByName(String name);
}

