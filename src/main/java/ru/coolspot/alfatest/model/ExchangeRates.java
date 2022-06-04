package ru.coolspot.alfatest.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public class ExchangeRates {
    private String disclaimer;
    private String license;
    private Timestamp timestamp;
    private String base;
    private Map<String, Double> rates = new HashMap<>();

    public ExchangeRates(String disclaimer, String license, Timestamp timestamp, String base, Map<String, Double> rates) {
        this.disclaimer = disclaimer;
        this.license = license;
        this.timestamp = timestamp;
        this.base = base;
        this.rates = rates;
    }
}
