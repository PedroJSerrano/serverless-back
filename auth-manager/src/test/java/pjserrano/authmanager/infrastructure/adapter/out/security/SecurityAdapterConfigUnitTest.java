package pjserrano.authmanager.infrastructure.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pjserrano.authmanager.domain.model.User;
import pjserrano.authmanager.domain.port.out.JwtSecretProviderPort;
import pjserrano.authmanager.domain.port.out.TokenServicePort;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios con mocks para verificar la lógica de generación de tokens JWT.
 * Prueban el comportamiento del adaptador de seguridad de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class SecurityAdapterConfigUnitTest {

    @Mock
    private JwtSecretProviderPort mockJwtSecretProvider;

    private TokenServicePort tokenServicePort;
    private SecurityAdapterConfig securityAdapterConfig;

    private static final String MOCK_SECRET = "unsecretoquesoloelservidorconoceyqueesmuylargoparaqueseguro123";
    private static final long JWT_EXPIRATION = 3600000L; // 1 hora

    @BeforeEach
    void setUp() {
        securityAdapterConfig = new SecurityAdapterConfig();
        ReflectionTestUtils.setField(securityAdapterConfig, "expiration", JWT_EXPIRATION);
        tokenServicePort = securityAdapterConfig.jwtTokenServiceAdapter(mockJwtSecretProvider);
    }

    @Test
    void whenJwtTokenIsGenerated_thenClaimsAreCorrect() {
        String username = "testuser";
        String password = "testpass";
        String email = "testuser@mail.com";
        List<String> roles = List.of("USER", "ADMIN");
        User user = new User(username, password, email, roles);

        when(mockJwtSecretProvider.get()).thenReturn(MOCK_SECRET);

        String token = tokenServicePort.apply(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        assertEquals(username, jws.getPayload().getSubject());
        assertEquals(roles, jws.getPayload().get("roles"));

        long now = System.currentTimeMillis();
        long expiryTime = jws.getPayload().getExpiration().getTime();
        assertTrue(expiryTime > now);
        assertTrue(expiryTime <= now + JWT_EXPIRATION + 1000); // +1s tolerancia
    }

    @Test
    void whenUserHasMultipleRoles_thenAllRolesAreIncludedInToken() {
        User user = new User("admin", "adminpass",
            "testuser@mail.com", List.of("USER", "ADMIN", "MODERATOR"));

        when(mockJwtSecretProvider.get()).thenReturn(MOCK_SECRET);

        String token = tokenServicePort.apply(user);

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        @SuppressWarnings("unchecked")
        List<String> tokenRoles = (List<String>) jws.getPayload().get("roles");
        assertEquals(3, tokenRoles.size());
        assertTrue(tokenRoles.contains("USER"));
        assertTrue(tokenRoles.contains("ADMIN"));
        assertTrue(tokenRoles.contains("MODERATOR"));
    }

    @Test
    void whenUserHasNoRoles_thenEmptyRolesListInToken() {
        User user = new User("basicuser", "basicpass", "testuser@mail.com", List.of());

        when(mockJwtSecretProvider.get()).thenReturn(MOCK_SECRET);

        String token = tokenServicePort.apply(user);

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        @SuppressWarnings("unchecked")
        List<String> tokenRoles = (List<String>) jws.getPayload().get("roles");
        assertNotNull(tokenRoles);
        assertTrue(tokenRoles.isEmpty());
    }

    @Test
    void whenTokenIsGenerated_thenIssuedAtAndExpirationAreSet() {
        User user = new User("timeuser", "timepass", "testuser@mail.com", List.of("USER"));

        when(mockJwtSecretProvider.get()).thenReturn(MOCK_SECRET);

        String token = tokenServicePort.apply(user);

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token);

        long issuedAt = jws.getPayload().getIssuedAt().getTime();
        long expiration = jws.getPayload().getExpiration().getTime();

        // Verificar que issuedAt es reciente (dentro de los últimos 5 segundos)
        long timeDifference = System.currentTimeMillis() - issuedAt;
        assertTrue(timeDifference >= 0 && timeDifference < 5000, "IssuedAt should be recent");

        // Verificar que expiration es issuedAt + JWT_EXPIRATION
        assertEquals(JWT_EXPIRATION, expiration - issuedAt, "Expiration should be issuedAt + JWT_EXPIRATION");
        
        // Verificar que el token no ha expirado aún
        assertTrue(expiration > System.currentTimeMillis(), "Token should not be expired yet");
    }

    @Test
    void whenDifferentUsers_thenDifferentTokensGenerated() {
        // GIVEN
        User user1 = new User("user1", "pass1", "testuser@mail.com", List.of("USER"));
        User user2 = new User("user2", "pass2", "testuser@mail.com", List.of("ADMIN"));

        when(mockJwtSecretProvider.get()).thenReturn(MOCK_SECRET);

        String token1 = tokenServicePort.apply(user1);
        String token2 = tokenServicePort.apply(user2);

        assertNotEquals(token1, token2);

        Jws<Claims> jws1 = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token1);
        
        Jws<Claims> jws2 = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(MOCK_SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token2);

        assertEquals("user1", jws1.getPayload().getSubject());
        assertEquals("user2", jws2.getPayload().getSubject());
    }
}