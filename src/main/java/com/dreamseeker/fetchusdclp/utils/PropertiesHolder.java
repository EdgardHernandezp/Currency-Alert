package com.dreamseeker.fetchusdclp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

final public class PropertiesHolder {
    private static final Properties properties;

    static {
        final String propertiesFileName = "app.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties = new Properties();
        try (InputStream input = loader.getResourceAsStream(propertiesFileName)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private PropertiesHolder() {
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
