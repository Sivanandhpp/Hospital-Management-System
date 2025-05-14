// src/main/java/com/hospitalmanagement/hmsbackend/security/JwtTokenProvider.java
package com.hospitalmanagement.hms_backend.security; // Your package

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys; // Ensure this import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets; // For converting string to bytes
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecretString; // Renamed for clarity

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private Key jwtSigningKey; // Store the derived key

    // Initialize the key once after properties are set
    @jakarta.annotation.PostConstruct // Or javax.annotation.PostConstruct if using older Spring Boot
    protected void init() {
        // Convert the plain string secret to bytes and then create the Key object
        this.jwtSigningKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256) // Use the initialized key
                .compact();
    }

    // Get username from JWT token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSigningKey) // Use the initialized key
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSigningKey) // Use the initialized key
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty or key is invalid: {}", e.getMessage());
        } // Add specific catch for SignatureException if needed
        catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT signature does not match locally computed signature: {}", e.getMessage());
        }
        return false;
    }
}