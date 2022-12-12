package com.dreamseeker.fetchusdclp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.dreamseeker.fetchusdclp.services.currencies.CurrencyRetrieverService;
import com.dreamseeker.fetchusdclp.services.currencies.Response;
import com.dreamseeker.fetchusdclp.services.email.EmailSender;
import com.dreamseeker.fetchusdclp.services.email.SESEmailSender;
import com.dreamseeker.fetchusdclp.utils.EnvironmentVariablesHolder;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO unit test
//TODO: Can i inject dependencies in this class? CurrencyRetriever and EmailSender
//TODO: prepare code for getting various currency, not just usd
public class Handler implements RequestHandler<Map<String, Object>, String> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        String lambdaExecResult = "Failed to retrieve currencies";
        CurrencyRetrieverService currencyRetrieverService = new CurrencyRetrieverService();
        Optional<Response> optionalResponse = currencyRetrieverService.retrieveCurrencyValues();
        if (!optionalResponse.isEmpty()) {
            lambdaExecResult = "It's not time to buy yet";
            Double buyingPrice = EnvironmentVariablesHolder.getBuyPrice();
            Double currentPrice = optionalResponse.get().getValue();
            logger.info("Current price: " + currentPrice + " | Buying price: " + buyingPrice);
            if (buyingPrice >= currentPrice) {
                logger.info("Time to buy!");
                EmailSender emailSender = new SESEmailSender();
                emailSender.sendEmail(buyingPrice, currentPrice);
                lambdaExecResult = "Time to buy";
            }
        }
        return lambdaExecResult;
    }
}
