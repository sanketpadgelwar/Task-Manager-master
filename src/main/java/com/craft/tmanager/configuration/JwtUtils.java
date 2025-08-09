package com.craft.tmanager.configuration;

import java.sql.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
    private String secret = "my-secret-key"; // use env/config in real apps

    public String generateToken(String username) {
            Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    return   Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(jwt);

    }
}
