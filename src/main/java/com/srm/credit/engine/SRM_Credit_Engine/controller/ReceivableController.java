package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.dto.SimulationRequestDTO;
import com.srm.credit.engine.SRM_Credit_Engine.dto.response.SimulationResponse;
import com.srm.credit.engine.SRM_Credit_Engine.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/receivables")
public class ReceivableController {

    private final PricingService service;

    public ReceivableController(PricingService service) {
        this.service = service;
    }

    @PostMapping("/simulate")
    @Operation(summary = "Simulação de recebiveis")
    public SimulationResponse simulate(
            @RequestBody @Valid SimulationRequestDTO req
    ) {

        BigDecimal result =
                service.calculate(
                        req.getFaceValue(),
                        req.getDaysToMaturity(),
                        req.getReceivableType()
                );

        return new SimulationResponse(result);
    }
}
