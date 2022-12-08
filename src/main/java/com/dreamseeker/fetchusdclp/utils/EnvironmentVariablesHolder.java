package com.dreamseeker.fetchusdclp.utils;

import java.util.Map;

final public class EnvironmentVariablesHolder {
    private static final Map<String, String> environmentVars = System.getenv();

    private EnvironmentVariablesHolder() {

    }

    public static String getBaseUrl() {
        return environmentVars.get("BASE_URL");
    }

    public static String getEndpointPath() {
        return environmentVars.get("ENDPOINT_PATH");
    }

    public static String getAppId() {
        return environmentVars.get("APP_ID");
    }

    public static String getSymbol() {
        return environmentVars.get("SYMBOLS");
    }

    public static Double getBuyPrice() {
        return Double.valueOf(environmentVars.get("BUY_PRICE"));
    }
}
