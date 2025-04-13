package com.currency.converter.util;

import com.currency.converter.exception.CurrencyConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CurrencyConverter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${exchange.rate.api.key}")
    private String apiKey;

    @Cacheable("exchangeRates")
    public double convert(String source, String target, double amount) {
        try {
            String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + source;
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
            if (response == null || !response.containsKey("rates")) {
                throw new CurrencyConversionException("Invalid response from exchange rate API");
            }

            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            Object rateObj = rates.get(target);

            if (rateObj == null) {
                throw new CurrencyConversionException("Target currency not found: " + target);
            }

            double rate = ((Number) rateObj).doubleValue();
            return amount * rate;
        } catch (Exception e) {
            throw new CurrencyConversionException("Currency conversion failed: " + e.getMessage());
        }
    }
}