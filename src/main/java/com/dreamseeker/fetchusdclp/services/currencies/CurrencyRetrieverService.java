package com.dreamseeker.fetchusdclp.services.currencies;

import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import com.dreamseeker.fetchusdclp.utils.ParserUtil;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: convert to interface so user can impl fetching from a different datasource
public class CurrencyRetrieverService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyRetrieverService.class);
    private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public Optional<Response> retrieveCurrencyValues() {
        Optional<Response> optionalResponse = Optional.empty();
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(buildUri()).build();
            //TODO: this class is generic, maybe I could returned the response as my domain class already instead of string
            //BodyHandlers seems to be the responsible of the return type
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("API HTTP response: \n" + httpResponse.toString());
            if (httpResponse.statusCode() == 200) //TODO: use a library to validate all 2XX
                optionalResponse = Optional.of(ParserUtil.parseResponse(httpResponse.body()));
        } catch (Exception e) {
            logger.error("Error trying to retrieve currency values", e);
        }
        return optionalResponse;
    }

    private URI buildUri() {
        String baseUrl = EnvironmentVariablesHolder.getBaseUrl();
        String endpointPath = EnvironmentVariablesHolder.getEndpointPath();
        String appId = EnvironmentVariablesHolder.getAppId();
        String symbols = EnvironmentVariablesHolder.getSymbol();

        StringBuilder stringBuilder = new StringBuilder();
        String completeUrl = stringBuilder.append(baseUrl).append(endpointPath).append("?").append("app_id=").append(appId).append("&")
                .append("symbols=").append(symbols).toString();
        URI uri = URI.create(completeUrl);
        return uri;
    }
}
