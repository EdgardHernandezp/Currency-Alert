package com.dreamseeker.fetchusdclp.utils;

import com.dreamseeker.fetchusdclp.services.currencies.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

final public class ParserUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private ParserUtil() {
    }

    public static Response parseResponse(String response) throws JsonProcessingException {
        return mapper.readValue(response, new TypeReference<Response>() {
        });
    }
}
