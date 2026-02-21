package com.example.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET_KEY = "c3VhLWNoYXZlLXNlY3JldGEtc3VwZXItc2VndXJhLXF1ZS1kZXZlLXNlci1iZW0tbG9uZ2E=";

    private SecretKey getSecretKey() {
        byte[] key = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    // Génère un token JWT avec validité de 1 heure
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSecretKey())
                .compact();
    }

    // Extrait les claims
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrait le username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Vérifie si expiré
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valide le token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);
    }
}
