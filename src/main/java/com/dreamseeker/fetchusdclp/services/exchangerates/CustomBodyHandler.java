package com.dreamseeker.fetchusdclp.services.exchangerates;

import com.dreamseeker.fetchusdclp.utils.ParserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.ResponseInfo;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBodyHandler implements BodyHandler<ExchangeRateInfo> {
    private static final Logger logger = LoggerFactory.getLogger(CustomBodyHandler.class);
    //TODO: ResponseInfo brings status code and headers, I can use it to validate response before parsing
    @Override
    public BodySubscriber<ExchangeRateInfo> apply(ResponseInfo responseInfo) {
        final HttpResponse.BodySubscriber<String> upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        return HttpResponse.BodySubscribers.mapping(upstream, body -> {
            try {
                return ParserUtil.parseResponse(body);
            } catch (JsonProcessingException e) {
                //TODO: throw exception or return null?
                logger.error("Error parsing input", e);
                return null;
            }
        });
    }
}