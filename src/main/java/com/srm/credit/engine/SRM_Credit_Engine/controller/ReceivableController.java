package com.srm.credit.engine.SRM_Credit_Engine.controller;

import com.srm.credit.engine.SRM_Credit_Engine.service.PricingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/receivables")
public class ReceivableController {

    private final PricingService pricingService;

    public ReceivableController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/simulate")
    public BigDecimal simulate(@RequestBody SimulationRequest request) {

        return pricingService.calculate(
                request.getFaceValue(),
                request.getDays(),
                request.getStrategy()
        );
    }
}
