package com.evolvetech.tollcalculator.service;

import com.evolvetech.tollcalculator.model.TollTaxCalculatorRequest;
import com.evolvetech.tollcalculator.model.TollTaxCalculatorResponse;

public interface TollCalculatorService {
    TollTaxCalculatorResponse getCalculatedTollTax(TollTaxCalculatorRequest taxCalculatorRequest);
}
