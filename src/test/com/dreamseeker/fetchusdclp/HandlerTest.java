package com.dreamseeker.fetchusdclp;

import com.amazonaws.services.lambda.runtime.Context;
import com.dreamseeker.fetchusdclp.services.email.EmailSender;
import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.dreamseeker.fetchusdclp.services.exchangerates.OpenExchangeRatesRetriever;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;
import static org.mockito.Mockito.*;

public class HandlerTest {
    @Mock
    private OpenExchangeRatesRetriever exchangeRatesRetriever;

    @Mock
    private EmailSender emailSender;

    @Mock
    private Context ctx;

    @InjectMocks
    private Handler handler;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(exchangeRatesRetriever);
        Assertions.assertNotNull(emailSender);
    }

    @Test
    public void whenErrorInApiReturnEmpty() {
        when(exchangeRatesRetriever.retrieveExchangeRates()).thenReturn(Optional.empty());
        String lambdaExecResult = "Failed to retrieve currencies";
        String invocationResult = handler.handleRequest(new HashMap<>(), ctx);
        Assertions.assertEquals(invocationResult, lambdaExecResult);
    }

    @Test
    public void whenIsNotTimeToBuyReturnMessage() throws Exception {
        ExchangeRateInfo exchangeRateInfo = new ExchangeRateInfo("USD", 850);
        when(exchangeRatesRetriever.retrieveExchangeRates()).thenReturn(Optional.of(exchangeRateInfo));
        String invocationResult = withEnvironmentVariable("BUY_PRICE", "800").execute(() -> handler.handleRequest(new HashMap<>(), ctx));
        String lambdaExecResult = "It's not time to buy yet";
        Assertions.assertEquals(invocationResult, lambdaExecResult);
    }

    @Test
    public void whenIsTimeToBuyReturnMessage() throws Exception {
        ExchangeRateInfo exchangeRateInfo = new ExchangeRateInfo("USD", 850);
        when(exchangeRatesRetriever.retrieveExchangeRates()).thenReturn(Optional.of(exchangeRateInfo));

        String invocationResult = withEnvironmentVariable("BUY_PRICE", "1000").execute(() -> handler.handleRequest(new HashMap<>(), ctx));
        verify(emailSender).sendEmail(anyDouble(), anyDouble());
        String lambdaExecResult = "Time to buy";
        Assertions.assertEquals(invocationResult, lambdaExecResult);
    }
}
