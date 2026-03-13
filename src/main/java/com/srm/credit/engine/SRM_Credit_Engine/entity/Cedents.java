package com.srm.credit.engine.SRM_Credit_Engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "cedents")
@Data
public class Cedents {
    @Id
    private UUID id;

    private String name;

    private String document;
}
