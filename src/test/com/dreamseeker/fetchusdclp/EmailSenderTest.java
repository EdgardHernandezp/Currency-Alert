package com.dreamseeker.fetchusdclp;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.dreamseeker.fetchusdclp.services.email.SESEmailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class EmailSenderTest {
    @InjectMocks
    private SESEmailSender emailSender;

    @Mock
    private AmazonSimpleEmailService amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.SA_EAST_1).build();
    @Mock
    private HttpResponse httpResponse;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(emailSender);
        Assertions.assertNotNull(amazonSimpleEmailService);
    }

    @Test
    public void whenSendEmailSuccessfully() {
        SendEmailResult sendEmailResult = new SendEmailResult();
        when(httpResponse.getStatusCode()).thenReturn(201);
        sendEmailResult.setSdkHttpMetadata(SdkHttpMetadata.from(httpResponse));
        when(amazonSimpleEmailService.sendEmail(any(SendEmailRequest.class))).thenReturn(sendEmailResult);

        emailSender.sendEmail(850, 820);
    }

    @Test
    public void whenSendEmailFails() {
        SendEmailResult sendEmailResult = new SendEmailResult();
        when(httpResponse.getStatusCode()).thenReturn(400);
        sendEmailResult.setSdkHttpMetadata(SdkHttpMetadata.from(httpResponse));
        when(amazonSimpleEmailService.sendEmail(any(SendEmailRequest.class))).thenReturn(sendEmailResult);

        emailSender.sendEmail(850, 820);
    }
}
