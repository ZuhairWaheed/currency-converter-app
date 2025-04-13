package com.currency.converter.service;

import com.currency.converter.dto.CalculationRequest;
import com.currency.converter.dto.CalculationResponse;

public interface BillingService {
    CalculationResponse calculate(CalculationRequest request);
}