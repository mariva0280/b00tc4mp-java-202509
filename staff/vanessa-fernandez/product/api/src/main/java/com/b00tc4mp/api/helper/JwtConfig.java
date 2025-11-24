package com.b00tc4mp.api.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.b00tc4mp.error.SystemException;

public final class JwtConfig {
    private static final String SECRET;
    private static final long EXPIRATION;

    static {
        Properties prop = new Properties();
        try (InputStream input = JwtConfig.class.getClassLoader()
        .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new SystemException("application.properties not found");
            }
            prop.load(input);
            SECRET = prop.getProperty("jwt.secret");
            EXPIRATION = Long.parseLong(prop.getProperty("jwt.expiration"));
        } catch (IOException e) {
            throw new SystemException ("Failed to load JWT config", e);
        }
    }

    public static String getSecret() {
        return SECRET;
    }

    public static long getExpiration() {
        return EXPIRATION;
    }
}
