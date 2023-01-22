package com.dreamseeker.fetchusdclp.services.exchangerates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

//TODO: design strategy to allow user to override this object so no errors while mapping this object with Jackson
@JsonIgnoreProperties({"disclaimer", "license", "timestamp"})
public class ExchangeRateInfo {
    private String base;
    private double value;

    public ExchangeRateInfo(String base, double value) {
        this.base = base;
        this.value = value;
    }
    @JsonCreator
    public ExchangeRateInfo(@JsonProperty("base") String base, @JsonProperty("rates") Map<String, Double> rates) {
        this.base = base;
        this.value = rates.get("CLP");
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
