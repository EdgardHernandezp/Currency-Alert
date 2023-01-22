package com.dreamseeker.fetchusdclp.services.exchangerates;

import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import java.net.URI;
import java.util.Optional;

//TODO: isolate http communication, make more generic so user can map response as it pleases
public class OpenExchangeRatesRetriever implements ExchangeRatesRetriever {

    private final HttpCourier courier;

    public OpenExchangeRatesRetriever(HttpCourier courier) {
        this.courier = courier;
    }

    @Override
    public Optional<ExchangeRateInfo> retrieveExchangeRates() {
        return courier.sendRequest(buildUri(), new CustomBodyHandler());
    }

    private URI buildUri() {
        String baseUrl = EnvironmentVariablesHolder.getBaseUrl();
        String endpointPath = EnvironmentVariablesHolder.getEndpointPath();
        String appId = EnvironmentVariablesHolder.getAppId();
        String symbols = EnvironmentVariablesHolder.getSymbol();

        StringBuilder stringBuilder = new StringBuilder();
        String completeUrl = stringBuilder.append(baseUrl).append(endpointPath).append("?").append("app_id=").append(appId).append("&")
                .append("symbols=").append(symbols).toString();
        return URI.create(completeUrl);
    }
}
