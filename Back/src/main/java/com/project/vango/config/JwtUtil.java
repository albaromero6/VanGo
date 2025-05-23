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
    public String generateToken(String nombre) {
        return Jwts.builder()
                .setSubject(nombre) // Nombre como sujeto del Token
                .claim("nombre", nombre) // Añadimos el nombre del usuario como claim
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256) // Firma del Token
                .compact(); // Genera el Token como String
    }

    // Método para extraer el nombre desde el Token
    public String extractNombre(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para validar que el nombre extraído coincide con el esperado
    public boolean validateToken(String token, String expectedNombre) {
        String extractedNombre = extractNombre(token);
        return extractedNombre.equals(expectedNombre) && !isTokenExpired(token);
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
