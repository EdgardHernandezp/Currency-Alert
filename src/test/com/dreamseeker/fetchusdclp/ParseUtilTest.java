package com.dreamseeker.fetchusdclp;

import com.dreamseeker.fetchusdclp.services.exchangerates.ExchangeRateInfo;
import com.dreamseeker.fetchusdclp.utils.ParserUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseUtilTest {
    @Test
    public void whenParseResponseReturnExchangeRatesInfo() throws IOException {
        String jsonResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/open-exchange-response.json")));
        System.out.println(jsonResponse);
        ExchangeRateInfo exchangeRateInfo = ParserUtil.parseResponse(jsonResponse);
        Assertions.assertThat(exchangeRateInfo.getBase()).isEqualTo("USD");
        Assertions.assertThat(exchangeRateInfo.getValue()).isNotNaN();
    }
}
