package com.currency.converter.controller;

import com.currency.converter.dto.CalculationRequest;
import com.currency.converter.dto.CalculationResponse;
import com.currency.converter.dto.Item;
import com.currency.converter.exception.CurrencyConversionException;
import com.currency.converter.service.BillingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillingController.class)
public class BillingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BillingService billingService;

    private CalculationRequest request;
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public BillingService billingService() {
            return mock(BillingService.class);
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Item item = new Item();
        item.setName("Laptop");
        item.setCategory("electronics");
        item.setPrice(1000);

        request = new CalculationRequest();
        request.setItems(List.of(item));
        request.setUserType("EMPLOYEE");
        request.setCustomerTenureInYears(1);
        request.setOriginalCurrency("USD");
        request.setTargetCurrency("EUR");
    }

    @Test
    void testSuccessfulCalculation() throws Exception {
        CalculationResponse response = new CalculationResponse();
        response.setOriginalAmount(1000);
        response.setDiscountedAmount(700);
        response.setFinalAmount(690);
        response.setTargetCurrency("EUR");

        Mockito.when(billingService.calculate(any())).thenReturn(response);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testExceptionHandling() throws Exception {
        Mockito.when(billingService.calculate(any()))
                .thenThrow(new CurrencyConversionException("Mocked failure"));

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}