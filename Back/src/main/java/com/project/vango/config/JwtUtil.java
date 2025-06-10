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

    // Método generador del Token usando el email
    public String generateToken(Long idUsu, String email, String nombre, String apellido, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("idUsu", idUsu)
                .claim("nombre", nombre)
                .claim("apellido", apellido)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Método para extraer el email desde el Token
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // Método para extraer el nombre desde el Token
    public String extractNombre(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("nombre", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Método para extraer el apellido desde el Token
    public String extractApellido(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("apellido", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Método para extraer el rol desde el Token
    public String extractRol(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("rol", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Método para validar que el email extraído coincide con el esperado
    public boolean validateToken(String token, String expectedEmail) {
        try {
            String extractedEmail = extractEmail(token);
            return extractedEmail != null && extractedEmail.equals(expectedEmail) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Método que devuelve true si el Token ya ha expirado
    private boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
