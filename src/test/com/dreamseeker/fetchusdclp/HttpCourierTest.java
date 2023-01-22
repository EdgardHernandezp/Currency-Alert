package com.dreamseeker.fetchusdclp;

import com.dreamseeker.fetchusdclp.dummies.DummyHttpClient;
import com.dreamseeker.fetchusdclp.dummies.DummyHttpResponse;
import com.dreamseeker.fetchusdclp.services.exchangerates.CustomBodyHandler;
import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.dreamseeker.fetchusdclp.services.exchangerates.HttpCourier;
import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class HttpCourierTest {

    @Mock
    private DummyHttpClient httpClient;
    @InjectMocks
    private HttpCourier httpCourier;
    private CustomBodyHandler customBodyHandler = new CustomBodyHandler();

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSendRequestSuccess() throws Exception {
        ExchangeRateInfo expectedExchangeRateInfo = new ExchangeRateInfo("CLP", 820);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(
                new DummyHttpResponse(expectedExchangeRateInfo));
        Optional<ExchangeRateInfo> actualExchangeRateInfo = httpCourier.sendRequest(getUri(), customBodyHandler);
        assertThat(actualExchangeRateInfo.isPresent()).isEqualTo(true);
        assertThat(actualExchangeRateInfo.get()).isEqualTo(expectedExchangeRateInfo);
    }

    @Test
    public void whenSendRequestFails() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(IOException.class);
        Optional<ExchangeRateInfo> optExchangeRateInfo = httpCourier.sendRequest(getUri(), customBodyHandler);
        assertThat(optExchangeRateInfo.isPresent()).isEqualTo(false);
    }

    private URI getUri() throws Exception {
        URI uri = SystemLambda.withEnvironmentVariable("BASE_URL", "https://openexchangerates.org/").and("ENDPOINT_PATH", "api/latest.json")
                .and("APP_ID", "833c87e7e2ca49baabf0efce616c5826").and("SYMBOLS", "CLP").execute(this::buildUri);
        return uri;
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
