package com.dreamseeker.fetchusdclp.services.exchangerates;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCourier {
    private static final Logger logger = LoggerFactory.getLogger(HttpCourier.class);
    private final HttpClient httpClient;

    public HttpCourier() {
        this.httpClient=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    public HttpCourier(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public <T> Optional<T> sendRequest(URI uri, HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
            HttpResponse<T> httpResponse = httpClient.send(request, bodyHandler);
            logger.info("API HTTP response: \n" + httpResponse.toString());
            return Optional.ofNullable(httpResponse.body());
        } catch (Exception e) {
            logger.error("Error trying to retrieve currency values", e);
            return Optional.empty();
        }
    }
}