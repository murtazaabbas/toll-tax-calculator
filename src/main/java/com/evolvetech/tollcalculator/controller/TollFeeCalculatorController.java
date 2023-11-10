package com.evolvetech.tollcalculator.controller;

import com.evolvetech.tollcalculator.model.TollTaxCalculatorRequest;
import com.evolvetech.tollcalculator.model.TollTaxCalculatorResponse;
import com.evolvetech.tollcalculator.service.TollCalculatorServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/toll-tax")
public class TollFeeCalculatorController {

    private final TollCalculatorServiceImpl calculatorService;

    public TollFeeCalculatorController(TollCalculatorServiceImpl calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<TollTaxCalculatorResponse> calculateTollTaxFee(@RequestBody TollTaxCalculatorRequest taxCalculatorRequest) {
        return ResponseEntity.ok(calculatorService.getCalculatedTollTax(taxCalculatorRequest));
    }
}
