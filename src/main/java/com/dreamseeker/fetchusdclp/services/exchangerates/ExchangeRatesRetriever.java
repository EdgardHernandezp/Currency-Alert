package com.dreamseeker.fetchusdclp.services.exchangerates;

import java.util.Optional;

public interface ExchangeRatesRetriever {
    Optional<ExchangeRateInfo> retrieveExchangeRates();
}
