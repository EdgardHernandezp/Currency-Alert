package com.dreamseeker.fetchusdclp.utils;

import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

final public class ParserUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private ParserUtil() {
    }

    public static ExchangeRateInfo parseResponse(String response) throws JsonProcessingException {
        return mapper.readValue(response, new TypeReference<ExchangeRateInfo>() {
        });
    }
}
