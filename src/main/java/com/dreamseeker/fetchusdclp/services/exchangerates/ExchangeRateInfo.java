package com.dreamseeker.fetchusdclp.services.exchangerates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

//TODO: design strategy to allow user to override this object so no errors while mapping this object with Jackson
@JsonIgnoreProperties({"disclaimer", "license", "timestamp"})
public class ExchangeRateInfo {
    private String base;
    private double value;

    public String getBase() {
        return base;
    }

    @JsonSetter("rates")
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
