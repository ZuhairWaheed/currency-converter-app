package com.currency.converter.service.impl;

import com.currency.converter.dto.*;
import com.currency.converter.exception.CurrencyConversionException;
import com.currency.converter.model.UserType;
import com.currency.converter.service.BillingService;
import com.currency.converter.util.CurrencyConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    private final CurrencyConverter currencyConverter;

    public BillingServiceImpl(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    @Override
    public CalculationResponse calculate(CalculationRequest request) {
        try {

            double discountedAmount = applyDiscounts(request.getItems(), request.getUserType(), request.getCustomerTenureInYears());
            double finalAmount = currencyConverter.convert(request.getOriginalCurrency(), request.getTargetCurrency(), discountedAmount);

            return getCalculationResponse(request, discountedAmount, finalAmount);
        } catch (Exception e) {
            throw new CurrencyConversionException("Failed to process the calculation: " + e.getMessage());
        }
    }

    private static CalculationResponse getCalculationResponse(CalculationRequest request, double discountedAmount, double finalAmount) {
        CalculationResponse response = new CalculationResponse();
        response.setOriginalAmount(request.getTotalAmount());
        response.setDiscountedAmount(discountedAmount);
        response.setFinalAmount(finalAmount);
        response.setTargetCurrency(request.getTargetCurrency());
        return response;
    }

    private double applyDiscounts(List<Item> items, String userTypeStr, int tenure) {
        double percentageDiscount = 0;
        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());

        percentageDiscount = getPercentageDiscount(tenure, userType, percentageDiscount);

        double cumulativeDiscount = getTotalDiscount(items, percentageDiscount);

        double additionalDiscount = Math.floor(cumulativeDiscount / 100) * 5;
        return cumulativeDiscount - additionalDiscount;
    }

    private static double getTotalDiscount(List<Item> items, double percentageDiscount) {
        double total = 0;
        for (Item item : items) {
            if (item.getCategory().equalsIgnoreCase("grocery")) {
                total += item.getPrice();
            } else {
                double discounted = item.getPrice() * (1 - percentageDiscount / 100);
                total += discounted;
            }
        }
        return total;
    }

    private static double getPercentageDiscount(int tenure, UserType userType, double percentageDiscount) {
        if (userType == UserType.EMPLOYEE) {
            percentageDiscount = 30;
        } else if (userType == UserType.AFFILIATE) {
            percentageDiscount = 10;
        } else if (tenure > 2) {
            percentageDiscount = 5;
        }
        return percentageDiscount;
    }
}