package com.dreamseeker.fetchusdclp;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, String> {
  // TODO unit test, find the way to test locally
  // TODO format code
  private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
  private static final String HTML_EMAIL_BODY =
          "<h1>Time to buy</h1>" + "<p>Your buying price: %s</p><p>Current price: %s</p>";
  private static final String UTF_8_CHARSET = "UTF-8";
  public static final String EMAIL = "edgardhernandezp@gmail.com";

  @Override public String handleRequest(Map<String, Object> event, Context context) {
    LambdaLogger logger = context.getLogger();
    URI uri = buildRequestUrl(logger);
    HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
    String responseBody = "No response";
    try {
      // TODO create a response type
      HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      responseBody = httpResponse.body();
      ObjectMapper objMapper = new ObjectMapper();
      TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
      };
      HashMap<String, Object> responseBodyObj = objMapper.readValue(responseBody, typeRef);
      logger.log("Response: " + responseBody + " | status code: " + httpResponse.statusCode());

      if (httpResponse.statusCode() == 200) { //TODO use library to checked all 2xx
        HashMap<String, Double> usdPricesPerCurrency = (HashMap<String, Double>) responseBodyObj.get("rates");
        Map<String, String> envVars = System.getenv();
        Double buyingPrice = Double.valueOf(envVars.get("BUY_PRICE"));
        Double currentPrice = usdPricesPerCurrency.get("CLP");
        if (buyingPrice >= currentPrice) {
          sendEmail(currentPrice, buyingPrice);
          logger.log("email sent successfully");
        }
      }
    } catch (IOException | InterruptedException e) {
      logger.log(e.getMessage());
    } catch (AmazonSimpleEmailServiceException e) {
      logger.log("error sending email");
      logger.log(e.getMessage());
    }

    return responseBody;
  }

  private void sendEmail(Double currentPrice, Double buyingPrice) throws AmazonSimpleEmailServiceException {
    AmazonSimpleEmailService sesService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.SA_EAST_1).build();
    final String emailBody = String.format(HTML_EMAIL_BODY, buyingPrice, currentPrice);

    SendEmailRequest emailRequest = new SendEmailRequest().withDestination(new Destination().withToAddresses(EMAIL)).withMessage(
            new Message().withBody(new Body().withHtml(new Content().withCharset(UTF_8_CHARSET).withData(emailBody)))
                    .withSubject(new Content().withCharset(UTF_8_CHARSET).withData("Price Alert"))).withSource(EMAIL);
    SendEmailResult sendEmailResult = sesService.sendEmail(emailRequest);

    //logger.log("Email Sending response status: " + httpStatusCode); //TODO init a static logger
    int httpStatusCode = sendEmailResult.getSdkHttpMetadata().getHttpStatusCode();
    if (httpStatusCode != 200) {
      throw new AmazonSimpleEmailServiceException("Attempt to send email failed. http status code: " + httpStatusCode);
    }
  }

  private URI buildRequestUrl(LambdaLogger logger) {
    Map<String, String> envVars = System.getenv();
    String baseUrl = envVars.get("BASE_URL");
    String endpointPath = envVars.get("ENDPOINT_PATH");
    String appId = envVars.get("APP_ID");
    String symbols = envVars.get("SYMBOLS");
    StringBuilder stringBuilder = new StringBuilder();
    String completeUrl = stringBuilder.append(baseUrl).append(endpointPath).append("?").append("app_id=").append(appId).append("&")
            .append("symbols=").append(symbols).toString();
    logger.log("Request URL -> " + completeUrl);
    URI uri = URI.create(completeUrl);
    return uri;
  }
        
}
    