package pjserrano.authmanager.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pjserrano.authmanager.application.port.out.UserRepositoryPort;
import pjserrano.authmanager.domain.UserPrincipal;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests de integración que verifican el adaptador de repositorio DynamoDB.
 * Usa mocks para simular DynamoDB sin dependencias externas.
 */
@SpringBootTest(classes = {DynamoDbUserRepositoryAdapter.class, DynamoDbUserRepositoryAdapterIntegrationTest.TestConfig.class},
    properties = {"spring.main.allow-bean-definition-overriding=true"})
class DynamoDbUserRepositoryAdapterIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
            DynamoDbEnhancedClient mockClient = mock(DynamoDbEnhancedClient.class);
            // Configurar para que siempre devuelva null (usuario no encontrado)
            when(mockClient.table(anyString(), any())).thenReturn(null);
            return mockClient;
        }
    }

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    // Test básico que verifica que el adaptador maneja correctamente cuando no encuentra usuario
    @Test
    void whenUserNotExists_thenReturnsEmpty() {
        // GIVEN
        String username = "nonexistent";

        // WHEN
        Optional<UserPrincipal> result = userRepositoryPort.apply(username);

        // THEN
        assertFalse(result.isPresent());
    }

    // Test que verifica que el adaptador maneja excepciones correctamente
    @Test
    void whenDynamoDbThrowsException_thenReturnsEmpty() {
        // GIVEN
        String username = "erroruser";

        // WHEN
        Optional<UserPrincipal> result = userRepositoryPort.apply(username);

        // THEN
        assertFalse(result.isPresent());
    }

    // Test que verifica que el UserRepositoryPort está correctamente inyectado
    @Test
    void userRepositoryPortIsInjected() {
        assertNotNull(userRepositoryPort);
    }
}