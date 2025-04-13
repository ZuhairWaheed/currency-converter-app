package com.currency.converter.controller;

import com.currency.converter.dto.CalculationRequest;
import com.currency.converter.dto.CalculationResponse;
import com.currency.converter.exception.CurrencyConversionException;
import com.currency.converter.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody CalculationRequest request) {
        try {
            CalculationResponse response = billingService.calculate(request);
            return ResponseEntity.ok(response);
        } catch (CurrencyConversionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}