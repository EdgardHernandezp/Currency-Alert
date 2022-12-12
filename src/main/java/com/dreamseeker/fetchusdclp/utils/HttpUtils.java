package com.dreamseeker.fetchusdclp.utils;

final public class HttpUtils {

    private HttpUtils() {
    }

    public static boolean is2xx(int statusCode) {
        return statusCode >= 200 && statusCode < 300 ? true : false;
    }
}
