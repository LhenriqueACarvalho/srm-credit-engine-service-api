package com.srm.credit.engine.SRM_Credit_Engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "receivable_types")
@Data
public class ReceivableType {

    @Id
    private UUID id;

    private String name;

    private BigDecimal spread;
}
