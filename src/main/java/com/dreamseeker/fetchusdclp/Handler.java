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
public class Handler implements RequestHandler<Map<String, Object>, Void> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        CurrencyRetrieverService currencyRetrieverService = new CurrencyRetrieverService();
        Optional<Response> optionalResponse = currencyRetrieverService.retrieveCurrencyValues();
        if (!optionalResponse.isEmpty()) {
            Double buyingPrice = EnvironmentVariablesHolder.getBuyPrice();
            Double currentPrice = optionalResponse.get().getValue();
            logger.info("Current price: " + currentPrice + " | Buying price: " + buyingPrice);
            if (buyingPrice >= currentPrice) {
                logger.info("Time to buy!");
                EmailSender emailSender = new SESEmailSender();
                emailSender.sendEmail(buyingPrice, currentPrice);
            }
        }
        //TODO: output execution result
        return null;
    }
}
