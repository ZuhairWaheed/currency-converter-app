package com.currency.converter.serviceImpl;

import com.currency.converter.dto.CalculationRequest;
import com.currency.converter.dto.CalculationResponse;
import com.currency.converter.dto.Item;
import com.currency.converter.service.impl.BillingServiceImpl;
import com.currency.converter.util.CurrencyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingServiceTests {

    private CurrencyConverter currencyConverter;
    private BillingServiceImpl billingService;

    @BeforeEach
    void setUp() {
        currencyConverter = Mockito.mock(CurrencyConverter.class);
        billingService = new BillingServiceImpl(currencyConverter);
    }

    @Test
    void testEmployeeDiscountAppliedCorrectly() {
        List<Item> items = List.of(
            createItem("Laptop", "electronics", 1000),
            createItem("Banana", "grocery", 100)
        );

        CalculationRequest request = new CalculationRequest();
        request.setItems(items);
        request.setUserType("EMPLOYEE");
        request.setCustomerTenureInYears(1);
        request.setOriginalCurrency("USD");
        request.setTargetCurrency("USD");

        when(currencyConverter.convert("USD", "USD", 765.0)).thenReturn(765.0); // (1000 * 0.7) + 100 = 800, - 35 = 765

        CalculationResponse response = billingService.calculate(request);

        assertEquals(1100.0, response.getOriginalAmount());
        assertEquals(765.0, response.getFinalAmount());
    }

    @Test
    void testAffiliateDiscountAppliedCorrectly() {
        List<Item> items = List.of(createItem("Phone", "electronics", 500));

        CalculationRequest request = new CalculationRequest();
        request.setItems(items);
        request.setUserType("AFFILIATE");
        request.setCustomerTenureInYears(1);
        request.setOriginalCurrency("USD");
        request.setTargetCurrency("USD");

        when(currencyConverter.convert("USD", "USD", 445.0)).thenReturn(445.0); // 500 * 0.9 = 450, - 5 = 445

        CalculationResponse response = billingService.calculate(request);
        assertEquals(500.0, response.getOriginalAmount());
        assertEquals(445.0, response.getFinalAmount());
    }

    @Test
    void testLoyalCustomerDiscountAppliedCorrectly() {
        List<Item> items = List.of(createItem("Tablet", "electronics", 200));

        CalculationRequest request = new CalculationRequest();
        request.setItems(items);
        request.setUserType("CUSTOMER");
        request.setCustomerTenureInYears(3);
        request.setOriginalCurrency("USD");
        request.setTargetCurrency("USD");

        when(currencyConverter.convert("USD", "USD", 185.0)).thenReturn(185.0); // 200 * 0.95 = 190, - 5 = 185

        CalculationResponse response = billingService.calculate(request);
        assertEquals(200.0, response.getOriginalAmount());
        assertEquals(185.0, response.getFinalAmount());
    }

    @Test
    void testNoPercentageDiscountForGrocery() {
        List<Item> items = List.of(createItem("Apple", "grocery", 120));

        CalculationRequest request = new CalculationRequest();
        request.setItems(items);
        request.setUserType("EMPLOYEE");
        request.setCustomerTenureInYears(1);
        request.setOriginalCurrency("USD");
        request.setTargetCurrency("USD");

        when(currencyConverter.convert("USD", "USD", 115.0)).thenReturn(115.0); // No percentage discount, $5 off for > $100

        CalculationResponse response = billingService.calculate(request);
        assertEquals(120.0, response.getOriginalAmount());
        assertEquals(115.0, response.getFinalAmount());
    }

    private Item createItem(String name, String category, double price) {
        Item item = new Item();
        item.setName(name);
        item.setCategory(category);
        item.setPrice(price);
        return item;
    }
}