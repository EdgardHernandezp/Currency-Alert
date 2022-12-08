package com.dreamseeker.fetchusdclp.services.email;

public interface EmailSender {
    void sendEmail(double buyingPrice, double currentPrice);
}
