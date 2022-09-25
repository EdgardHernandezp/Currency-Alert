package com.dreamseeker.fetchusdclp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, String> {

	private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

	@Override
	public String handleRequest(Map<String, Object> event, Context context) {
		LambdaLogger logger = context.getLogger();
		// TODO parameterized baseUrl, endpoint path, symbols
		// Use env vars for this
		URI uri = URI.create(
				"https://openexchangerates.org/api/latest.json?app_id=833c87e7e2ca49baabf0efce616c5826&symbols=CLP");
		HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
		String response = "No response";
		try {
			// TODO create a response type
			java.net.http.HttpResponse<String> httpResponse = httpClient.send(request,
					java.net.http.HttpResponse.BodyHandlers.ofString());
			logger.log("Response: " + httpResponse.body() + " | status code: " + httpResponse.statusCode());
		} catch (IOException | InterruptedException e) {
			logger.log(e.getMessage());
		}

		return response;
	}

}
