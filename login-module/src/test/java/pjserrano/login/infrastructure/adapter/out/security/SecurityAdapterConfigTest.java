package pjserrano.login.infrastructure.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pjserrano.login.application.port.out.TokenServicePort;
import pjserrano.login.domain.UserPrincipal;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SecurityAdapterConfig.class)
@TestPropertySource(properties = {
        "jwt.secret=unsecretoquesoloelservidorconoceyqueesmuylargoparaqueseguro123",
        "jwt.expiration=3600000" // 1 hora
})
class SecurityAdapterConfigTest {

    @Autowired
    private TokenServicePort tokenServicePort;

    @Test
    void whenJwtTokenIsGenerated_thenClaimsAreCorrect() {
        // GIVEN:
        // Datos de prueba para el UserPrincipal
        String username = "testuser";
        String password = "testuser";
        List<String> roles = List.of("USER", "ADMIN");
        UserPrincipal userPrincipal = new UserPrincipal(username, password, roles);

        // WHEN:
        // Genera el token usando el puerto inyectado
        String token = tokenServicePort.apply(userPrincipal);

        // THEN:
        // 1. Verificamos que el token no es nulo ni está vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 2. Usamos el mismo secreto para decodificar el token y verificar sus claims
        String secretKey = "unsecretoquesoloelservidorconoceyqueesmuylargoparaqueseguro123";
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        // 3. Verificamos el subject y los roles del token
        assertEquals(username, jws.getPayload().getSubject());
        assertEquals("USER,ADMIN", jws.getPayload().get("roles"));

        // 4. Verificamos la fecha de expiración
        long expirationMillis = 3600000L; // 1 hora
        long now = System.currentTimeMillis();
        long expiryTime = jws.getPayload().getExpiration().getTime();
        assertTrue(expiryTime > now);
    }
}