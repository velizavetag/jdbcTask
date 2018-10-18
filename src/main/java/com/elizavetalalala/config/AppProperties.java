package com.elizavetalalala.config;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;


public class AppProperties {
    private static final String CONFIG_FILE_NAME = "app.properties";

    private static AppProperties instance;

    private final Properties properties = new Properties();

    private AppProperties() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        properties.load(inputStream);
    }

    public synchronized static AppProperties getProperties() {

        if (instance == null) {
            try {
                instance = new AppProperties();
            } catch (IOException e) {
                System.out.println("Something went wrong while getting property file " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public String getProperty(String key) {

        return properties.getProperty(key);
    }
}
