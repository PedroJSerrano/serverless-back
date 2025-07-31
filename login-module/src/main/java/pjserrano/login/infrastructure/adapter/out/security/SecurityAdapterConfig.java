package pjserrano.login.infrastructure.adapter.out.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pjserrano.login.application.port.out.TokenServicePort;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Configuration
public class SecurityAdapterConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Razón: El puerto de salida ahora recibe un UserPrincipal.
    // La lógica de la lambda se modifica para añadir los roles como un claim.
    @Bean
    public TokenServicePort jwtTokenServiceAdapter() {
        return userPrincipal -> {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .subject(userPrincipal.getUsername())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    // Razón: Se añade un claim personalizado 'roles'.
                    .claim("roles", userPrincipal.getRoles().stream().collect(Collectors.joining(",")))
                    .signWith(key)
                    .compact();
        };
    }
}