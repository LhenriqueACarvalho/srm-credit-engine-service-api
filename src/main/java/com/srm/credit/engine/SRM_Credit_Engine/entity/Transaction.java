package com.srm.credit.engine.SRM_Credit_Engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    private UUID id;

    @ManyToOne
    private Receivable receivable;

    private BigDecimal presentValue;

    private BigDecimal exchangeRate;

    private BigDecimal finalValue;

    private LocalDateTime createdAt;
}
