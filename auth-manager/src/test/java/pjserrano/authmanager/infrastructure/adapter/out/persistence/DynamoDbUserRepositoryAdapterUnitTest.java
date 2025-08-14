package pjserrano.authmanager.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.authmanager.domain.model.User;
import pjserrano.authmanager.domain.port.out.UserRepositoryPort;
import pjserrano.authmanager.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test unitario para DynamoDbUserRepositoryAdapter.
 * Usa mocks para testear solo la lógica del adaptador.
 */
@ExtendWith(MockitoExtension.class)
class DynamoDbUserRepositoryAdapterUnitTest {

    @Mock
    private DynamoDbEnhancedClient mockEnhancedClient;

    @Mock
    private DynamoDbTable<UserDynamoEntity> mockUserTable;

    private DynamoDbUserRepositoryAdapter adapter;
    private UserRepositoryPort userRepositoryPort;

    @BeforeEach
    void setUp() {
        adapter = new DynamoDbUserRepositoryAdapter(mockEnhancedClient);
        
        // Configurar el mock para que devuelva la tabla mockeada (cast necesario para tipos genéricos)
        when(mockEnhancedClient.table(any(String.class), any())).thenReturn((DynamoDbTable) mockUserTable);
        
        // Obtener el puerto configurado por el adaptador
        userRepositoryPort = adapter.userRepositoryPort();
    }

    @Test
    void whenUserExists_thenReturnsUserWithCorrectRoles() {
        // GIVEN
        String username = "testuser";
        UserDynamoEntity mockEntity = UserDynamoEntity.builder()
                .username(username)
                .password("encoded_password")
                .roles(List.of("USER", "ADMIN"))
                .build();

        when(mockUserTable.getItem(any(Key.class))).thenReturn(mockEntity);

        // WHEN
        Optional<User> result = userRepositoryPort.apply(username);

        // THEN
        assertTrue(result.isPresent(), "User should be found");
        User user = result.get();
        assertEquals(username, user.getUsername());
        assertEquals("encoded_password", user.getPassword());
        assertIterableEquals(List.of("USER", "ADMIN"), user.getRoles());
    }

    @Test
    void whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // GIVEN
        String username = "nonexistentuser";
        when(mockUserTable.getItem(any(Key.class))).thenReturn(null);

        // WHEN
        Optional<User> result = userRepositoryPort.apply(username);

        // THEN
        assertFalse(result.isPresent(), "Non-existent user should return empty");
    }

    @Test
    void whenUserHasNoRoles_thenReturnsEmptyRolesList() {
        // GIVEN
        String username = "noroleuser";
        UserDynamoEntity mockEntity = UserDynamoEntity.builder()
                .username(username)
                .password("password")
                .roles(null) // Sin roles
                .build();

        when(mockUserTable.getItem(any(Key.class))).thenReturn(mockEntity);

        // WHEN
        Optional<User> result = userRepositoryPort.apply(username);

        // THEN
        assertTrue(result.isPresent(), "User without roles should be found");
        User user = result.get();
        assertEquals(username, user.getUsername());
        assertTrue(user.getRoles().isEmpty(), "Roles should be empty list, not null");
    }

    @Test
    void whenDynamoDbThrowsException_thenReturnsEmptyOptional() {
        // GIVEN
        String username = "erroruser";
        when(mockUserTable.getItem(any(Key.class))).thenThrow(new RuntimeException("DynamoDB error"));

        // WHEN
        Optional<User> result = userRepositoryPort.apply(username);

        // THEN
        assertFalse(result.isPresent(), "Should return empty when DynamoDB fails");
    }
}