package dev.pedronube.authenticationservice.infrastructure.adapter.out.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pedronube.authenticationservice.domain.port.out.JwtSecretProviderPort;
import dev.pedronube.authenticationservice.domain.port.out.TokenServicePort;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Configuration
public class SecurityAdapterConfig {

    //@Value("${JWT_EXPIRATION}")
    private long expiration = 36000000;

    /* Se genera un token con los datos del usuario, incluidos los roles para
    la securización que realizará AWS API Gateway */
    @Bean
    public TokenServicePort jwtTokenServiceAdapter(JwtSecretProviderPort jwtSecretProvider) {
        return userPrincipal -> {
            // Obtenemos el secreto usando la interfaz del proveedor
            String secret = jwtSecretProvider.get();

            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            return Jwts.builder()
                    .subject(userPrincipal.getUsername())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .claim("roles", userPrincipal.getRoles())
                    .signWith(key)
                    .compact();
        };
    }
}