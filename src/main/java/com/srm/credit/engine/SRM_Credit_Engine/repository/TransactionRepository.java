package com.srm.credit.engine.SRM_Credit_Engine.repository;

import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
