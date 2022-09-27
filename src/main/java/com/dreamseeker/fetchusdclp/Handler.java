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
	// TODO automate lambda upload to aws, use Serverless framework
	// TODO unit test, find the way to test locally
	private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

	@Override
	public String handleRequest(Map<String, Object> event, Context context) {
		LambdaLogger logger = context.getLogger();
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
		HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
		String responseBody = "No response";
		try {
			// TODO create a response type
			java.net.http.HttpResponse<String> httpResponse = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
			responseBody = httpResponse.body();
			logger.log("Response: " + responseBody + " | status code: " + httpResponse.statusCode());
		} catch (IOException | InterruptedException e) {
			logger.log(e.getMessage());
		}

		return responseBody;
	}

}
