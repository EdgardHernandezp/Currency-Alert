package com.dreamseeker.fetchusdclp;

import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.dreamseeker.fetchusdclp.services.exchangerates.HttpCourier;
import com.dreamseeker.fetchusdclp.services.exchangerates.OpenExchangeRatesRetriever;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenExchangeRetrieverTest {

    @Mock
    private HttpCourier httpCourier;
    @InjectMocks
    private OpenExchangeRatesRetriever openExchangesRatesRetriever;

    @BeforeAll
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenRetrieveExchangesRatesReturnPopulatedOptional() {
        ExchangeRateInfo exchangeRateInfo = new ExchangeRateInfo("CLP", 820);
        Mockito.when(httpCourier.sendRequest(Mockito.any(URI.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(Optional.of(exchangeRateInfo));

        Optional<ExchangeRateInfo> optExchangeRateInfo = openExchangesRatesRetriever.retrieveExchangeRates();
        assertThat(optExchangeRateInfo.isPresent()).isEqualTo(true);
        assertThat(optExchangeRateInfo.get()).isEqualTo(exchangeRateInfo);
    }
}
