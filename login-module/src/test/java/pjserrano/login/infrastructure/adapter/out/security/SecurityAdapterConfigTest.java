package pjserrano.login.infrastructure.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pjserrano.login.application.port.out.JwtSecretProviderPort;
import pjserrano.login.application.port.out.TokenServicePort;
import pjserrano.login.domain.UserPrincipal;

import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SecurityAdapterConfig.class, properties = {"JWT_EXPIRATION=3600000"})
class SecurityAdapterConfigTest {

    // Mockeamos el proveedor del secreto para aislar el test del Parameter Store de AWS
    @MockBean
    private JwtSecretProviderPort jwtSecretProvider;

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

        // Secreto de mock que usaremos tanto para la inyección como para la verificación
        String mockSecret = "unsecretoquesoloelservidorconoceyqueesmuylargoparaqueseguro123";

        // WHEN:
        // 1. Configuramos el mock para que devuelva nuestro secreto
        when(jwtSecretProvider.get()).thenReturn(mockSecret);

        // 2. Genera el token usando el puerto inyectado
        String token = tokenServicePort.apply(userPrincipal);

        // THEN:
        // 1. Verificamos que el token no es nulo ni está vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 2. Usamos el mismo secreto de mock para decodificar el token y verificar sus claims
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(mockSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        // 3. Verificamos el subject y los roles del token
        assertEquals(username, jws.getPayload().getSubject());
        assertEquals(roles, jws.getPayload().get("roles"));

        // 4. Verificamos la fecha de expiración
        long now = System.currentTimeMillis();
        long expiryTime = jws.getPayload().getExpiration().getTime();
        assertTrue(expiryTime > now);
    }
}