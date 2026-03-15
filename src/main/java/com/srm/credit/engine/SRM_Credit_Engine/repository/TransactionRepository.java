package com.srm.credit.engine.SRM_Credit_Engine.repository;

import com.srm.credit.engine.SRM_Credit_Engine.dto.TransactionStatementDTO;
import com.srm.credit.engine.SRM_Credit_Engine.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = """
        SELECT 
            t.id as transactionId,
            r.id as receivableId,
            c.name as cedentName,
            c.document as cedentDocument,
            rt.name as receivableType,
            r.face_value as faceValue,
            cur.code as currency,
            t.present_value as presentValue,
            t.exchange_rate as exchangeRate,
            t.final_value as finalValue,
            r.maturity_date as maturityDate,
            t.created_at as createdAt
        FROM transactions t
        JOIN receivables r ON t.receivable_id = r.id
        JOIN receivable_types rt ON r.type_id = rt.id
        JOIN currencies cur ON r.currency_id = cur.id
        LEFT JOIN cedents c ON r.cedent_id = c.id
        WHERE 
            (t.created_at::date >= :startDate OR :startDate IS NULL)
            AND (t.created_at::date <= :endDate OR :endDate IS NULL)
            AND (cur.code = :currency OR :currency IS NULL)
            AND (c.name ILIKE :cedent OR :cedent IS NULL)
        ORDER BY t.created_at DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM transactions t
        JOIN receivables r ON t.receivable_id = r.id
        LEFT JOIN cedents c ON r.cedent_id = c.id
        JOIN currencies cur ON r.currency_id = cur.id
        WHERE 
            (t.created_at::date >= :startDate OR :startDate IS NULL)
            AND (t.created_at::date <= :endDate OR :endDate IS NULL)
            AND (cur.code = :currency OR :currency IS NULL)
            AND (c.name ILIKE :cedent OR :cedent IS NULL)
        """,
            nativeQuery = true)
    Page<TransactionStatementDTO> findLiquidationStatement(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("currency") String currency,
            @Param("cedent") String cedent,
            Pageable pageable);
}
