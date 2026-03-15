package com.srm.credit.engine.SRM_Credit_Engine.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SimulationResponse {
    private BigDecimal presentValue;

}
