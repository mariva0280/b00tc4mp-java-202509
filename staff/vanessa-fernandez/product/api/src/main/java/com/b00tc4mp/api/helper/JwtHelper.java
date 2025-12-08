package com.b00tc4mp.api.helper;

import java.util.Date;

import javax.crypto.SecretKey;

import com.b00tc4mp.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtHelper {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            Config.getJwtSecret().getBytes()
    );

    private static final long EXPIRATION_MS = Config.getJwtExpiration();

    public static String issueToken(String userId) {
        return Jwts.builder()
                .subject(userId)               
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String validateToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
}
