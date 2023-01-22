package com.dreamseeker.fetchusdclp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.dreamseeker.fetchusdclp.services.email.EmailSender;
import com.dreamseeker.fetchusdclp.services.email.SESEmailSender;
import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRatesRetriever;
import com.dreamseeker.fetchusdclp.services.exchangerates.HttpCourier;
import com.dreamseeker.fetchusdclp.services.exchangerates.OpenExchangeRatesRetriever;
import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: prepare code for getting various currency, not just usd
//TODO: create shells to run locally and remotely
public class Handler implements RequestHandler<Map<String, Object>, String> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private final ExchangeRatesRetriever exchangeRatesRetriever;
    private final EmailSender emailSender;

    public Handler(ExchangeRatesRetriever exchangeRatesRetriever, EmailSender emailSender) {
        this.exchangeRatesRetriever = exchangeRatesRetriever;
        this.emailSender = emailSender;
    }

    public Handler() {
        this.exchangeRatesRetriever = new OpenExchangeRatesRetriever(new HttpCourier());
        this.emailSender = new SESEmailSender();
    }

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        String lambdaExecResult = "Failed to retrieve currencies";
        Optional<ExchangeRateInfo> optionalResponse = exchangeRatesRetriever.retrieveExchangeRates();
        if (!optionalResponse.isEmpty()) {
            lambdaExecResult = "It's not time to buy yet";
            Double buyingPrice = EnvironmentVariablesHolder.getBuyPrice();
            Double currentPrice = optionalResponse.get().getValue();
            logger.info("Current price: " + currentPrice + " | Buying price: " + buyingPrice);
            if (buyingPrice >= currentPrice) {
                logger.info("Time to buy!");
                emailSender.sendEmail(buyingPrice, currentPrice);
                lambdaExecResult = "Time to buy";
            }
        }
        return lambdaExecResult;
    }
}
