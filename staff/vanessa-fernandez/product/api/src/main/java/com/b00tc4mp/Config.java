package com.b00tc4mp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.b00tc4mp.error.SystemException;

public final class Config {
    private static final String JWT_SECRET;
    private static final long JWT_EXPIRATION;
    private static final String DB_URI;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        Properties prop = new Properties();
        try (InputStream input = Config.class.getClassLoader()
        .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new SystemException("application.properties not found");
            }
            prop.load(input);
            JWT_SECRET = prop.getProperty("jwt.secret");
            JWT_EXPIRATION = Long.parseLong(prop.getProperty("jwt.expiration"));
            DB_URI = prop.getProperty("db.uri");
            DB_USER = prop.getProperty("db.user");  
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException e) {
            throw new SystemException ("Failed to load JWT config", e);
        }
    }

    public static String getJwtSecret() {
        return JWT_SECRET;
    }

    public static long getJwtExpiration() {
        return JWT_EXPIRATION;
    }

    public static String getDbUri() {
        return DB_URI;
    }

    public static String getDbUser() {
        return DB_USER;
    }
    
    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}
