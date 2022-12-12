package com.dreamseeker.fetchusdclp.services.currencies;

import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import com.dreamseeker.fetchusdclp.utils.HttpUtils;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: convert to interface so user can impl fetching from a different datasource
//TODO: isolate http communication, make more generic so user can map response as it pleases
public class CurrencyRetrieverService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyRetrieverService.class);
    private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public Optional<Response> retrieveCurrencyValues() {
        Optional<Response> optionalResponse = Optional.empty();
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(buildUri()).build();
            HttpResponse<Response> httpResponse = httpClient.send(request, new CustomBodyHandler());
            logger.info("API HTTP response: \n" + httpResponse.toString());
            if (HttpUtils.is2xx(httpResponse.statusCode()))
                optionalResponse = Optional.of(httpResponse.body());
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
