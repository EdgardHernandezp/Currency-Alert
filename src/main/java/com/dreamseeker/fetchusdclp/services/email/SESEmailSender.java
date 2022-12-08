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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class SESEmailSender implements EmailSender {
    public static final String EMAIL = "edgardhernandezp@gmail.com";
    private static final Logger logger = LoggerFactory.getLogger(SESEmailSender.class);
    private static final AmazonSimpleEmailService sesService = AmazonSimpleEmailServiceClientBuilder.standard()
            .withRegion(Regions.SA_EAST_1).build();
    private static final String HTML_EMAIL_BODY = "<h1>Time to buy</h1>" + "<p>Your buying price: %s</p><p>Current price: %s</p>";
    private static final String UTF_8_CHARSET = "UTF-8";

    @Override
    public void sendEmail(double buyingPrice, double currentPrice) {
        final String emailBody = String.format(HTML_EMAIL_BODY, buyingPrice, currentPrice);
        SendEmailRequest emailRequest = generateEmailRequest(emailBody);
        SendEmailResult sendEmailResult = sesService.sendEmail(emailRequest);

        logger.info("Attempting to send email");
        int httpStatusCode = sendEmailResult.getSdkHttpMetadata().getHttpStatusCode();
        if (httpStatusCode != 200)
            logger.error("Attempt to send email failed. http status code: " + httpStatusCode);
        else
            logger.info("Email sent");
    }

    private SendEmailRequest generateEmailRequest(String emailBody) {
        return new SendEmailRequest().withDestination(new Destination().withToAddresses(EMAIL)).withMessage(
                new Message().withBody(new Body().withHtml(new Content().withCharset(UTF_8_CHARSET).withData(emailBody)))
                        .withSubject(new Content().withCharset(UTF_8_CHARSET).withData("Price Alert"))).withSource(EMAIL);
    }
}
