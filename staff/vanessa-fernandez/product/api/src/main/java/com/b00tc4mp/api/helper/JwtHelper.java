package com.b00tc4mp.api.helper;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtHelper {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            JwtConfig.getSecret().getBytes()
    );

    private static final long EXPIRATION_MS = JwtConfig.getExpiration();

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
