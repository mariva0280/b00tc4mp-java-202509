package com.b00tc4mp.logic.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.b00tc4mp.error.SystemException;

public final class Config {

    private static final String API_URL;

    static {
        
        try {
            Properties prop = new Properties();

            InputStream input = Config.class.getClassLoader()
                    .getResourceAsStream("application.properties");

            if (input == null) {
                throw new SystemException("application.properties not found");
            }
            prop.load(input);
            API_URL = prop.getProperty("api.url");

        } catch (IOException e) {
            throw new SystemException("Failed to load JWT config", e);
        }
    }

    public static String getApiUrl() {
        return API_URL;
    }
}
