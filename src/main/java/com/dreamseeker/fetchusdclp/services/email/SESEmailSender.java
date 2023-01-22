package com.dreamseeker.fetchusdclp.services.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.dreamseeker.fetchusdclp.utils.HttpUtils;
import com.dreamseeker.fetchusdclp.utils.PropertiesHolder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SESEmailSender implements EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(SESEmailSender.class);
    private final AmazonSimpleEmailService sesService;
    //TODO: pass to html file
    private static final String HTML_EMAIL_BODY = "<h1>Time to buy</h1>" + "<p>Your buying price: %s</p><p>Current price: %s</p>";
    private static final String RECIPIENT_PROP_KEY = "notification.service.email.recipient";

    public SESEmailSender() {
        this.sesService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.SA_EAST_1).build();
    }

    public SESEmailSender(AmazonSimpleEmailService sesService) {
        this.sesService = sesService;
    }

    @Override
    public void sendEmail(double buyingPrice, double currentPrice) {
        final String emailBody = String.format(HTML_EMAIL_BODY, buyingPrice, currentPrice);
        SendEmailRequest emailRequest = generateEmailRequest(emailBody);
        SendEmailResult sendEmailResult = sesService.sendEmail(emailRequest);

        logger.info("Attempting to send email");
        int httpStatusCode = sendEmailResult.getSdkHttpMetadata().getHttpStatusCode();
        if (!HttpUtils.is2xx(httpStatusCode))
            logger.error("Attempt to send email failed. http status code: " + httpStatusCode);
        else
            logger.info("Email sent");
    }

    private SendEmailRequest generateEmailRequest(String emailBody) {
        final String utf8Charset = StandardCharsets.UTF_8.displayName();
        //TODO: permit various emails and accounts with different buying/selling prices
        final String recipientEmail = PropertiesHolder.getProperty(RECIPIENT_PROP_KEY);
        return new SendEmailRequest().withDestination(new Destination().withToAddresses(recipientEmail)).withMessage(
                new Message().withBody(new Body().withHtml(new Content().withCharset(utf8Charset).withData(emailBody)))
                        .withSubject(new Content().withCharset(utf8Charset).withData("Price Alert"))).withSource(recipientEmail);
    }
}
