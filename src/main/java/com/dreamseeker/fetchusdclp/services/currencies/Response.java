package com.dreamseeker.fetchusdclp.services.currencies;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Map;

@JsonIgnoreProperties({"disclaimer", "license"})
public class Response {
    private long timestamp;
    private String base;
    private Double value;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @JsonSetter("rates")
    public void setValue(Map<String, Double> rates) {
        this.value = rates.get("CLP");
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getBase() {
        return base;
    }

    public Double getValue() {
        return value;
    }
}
