package com.srm.credit.engine.SRM_Credit_Engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "receivables")
@Data
public class Receivable {

    @Id
    private UUID id;

    @ManyToOne
    private ReceivableType type;

    private BigDecimal faceValue;

    @ManyToOne
    private Currency currency;

    private LocalDate maturityDate;
}
