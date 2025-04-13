package com.currency.converter.dto;

import java.util.List;

public class CalculationRequest {
    private List<Item> items;
    private double totalAmount;
    private String originalCurrency;
    private String targetCurrency;
    private String userType;
    private int customerTenureInYears;

    // Getters and SettersC

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getCustomerTenureInYears() {
        return customerTenureInYears;
    }

    public void setCustomerTenureInYears(int customerTenureInYears) {
        this.customerTenureInYears = customerTenureInYears;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}