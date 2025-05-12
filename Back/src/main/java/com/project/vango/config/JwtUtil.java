package com.project.vango.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    // Constructor para inyectar la clave desde application.properties
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Método generador del Token usando el email como subject
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)                                     // Email como sujeto del Token
                .setIssuedAt(new Date())                               // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256)               // Firma del Token
                .compact();                                            // Genera el Token como String
    }

    // Método para extraer el email desde el Token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para validar que el email extraído coincide con el esperado
    public boolean validateToken(String token, String expectedEmail) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(expectedEmail) && !isTokenExpired(token);
    }

    // Método que devuelve true si el Token ya ha expirado
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
