package pjserrano.login.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pjserrano.login.application.port.out.UserRepositoryPort;
import pjserrano.login.config.DynamoDbConfig;
import pjserrano.login.domain.UserPrincipal;
import pjserrano.login.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para DynamoDbUserRepositoryAdapter.
 * Usa LocalStack para probar la integración real con DynamoDB.
 */
@Testcontainers
@SpringBootTest(classes = {DynamoDbConfig.class})
@Import(DynamoDbUserRepositoryAdapter.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DynamoDbUserRepositoryAdapterIntegrationTest {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");

    @Container
    public static LocalStackContainer localstack =
            new LocalStackContainer(LOCALSTACK_IMAGE)
                    .withServices(LocalStackContainer.Service.DYNAMODB);

    @DynamicPropertySource
    static void registerDynamoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
        registry.add("spring.cloud.aws.region.static", () -> localstack.getRegion());
        registry.add("spring.cloud.aws.credentials.access-key", () -> localstack.getAccessKey());
        registry.add("spring.cloud.aws.credentials.secret-key", () -> localstack.getSecretKey());
    }

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbTable<UserDynamoEntity> userTable;

    private static boolean tableCreated = false;

    @BeforeEach
    void setupTable() {
        if (!tableCreated) {
            createTableSafely();
            insertTestData();
            tableCreated = true;
        }
    }

    private void createTableSafely() {
        try {
            // Intentar eliminar tabla existente primero
            try {
                userTable.deleteTable();
                // Esperar a que se elimine completamente
                waitForTableDeletion();
            } catch (ResourceNotFoundException e) {
                // La tabla no existía, está bien
            }
            
            // Crear nueva tabla
            userTable.createTable();
            // Esperar a que esté realmente activa
            waitForTableCreation();
            
        } catch (ResourceInUseException e) {
            // La tabla ya existe, esperar a que esté activa
            try {
                waitForTableCreation();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while setting up table", e);
        }
    }

    private void waitForTableCreation() throws InterruptedException {
        // Esperar hasta que la tabla esté realmente activa para operaciones de escritura
        int maxAttempts = 30; // 30 segundos máximo
        for (int i = 0; i < maxAttempts; i++) {
            try {
                // Intentar una operación de escritura real para verificar que la tabla está activa
                UserDynamoEntity testEntity = UserDynamoEntity.builder()
                        .username("__test_table_ready__")
                        .password("test")
                        .roles(List.of())
                        .build();
                        
                userTable.putItem(testEntity);
                
                // Si llegamos aquí, la tabla está lista. Limpiar el item de prueba
                try {
                    userTable.deleteItem(testEntity);
                } catch (Exception ignored) {
                    // No importa si falla la eliminación
                }
                
                return; // Tabla está activa para escritura
            } catch (Exception e) {
                Thread.sleep(1000); // Esperar 1 segundo antes del siguiente intento
            }
        }
        throw new RuntimeException("Table creation timeout after 30 seconds");
    }

    private void waitForTableDeletion() throws InterruptedException {
        // Esperar hasta que la tabla esté completamente eliminada
        int maxAttempts = 30;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                userTable.describeTable();
                Thread.sleep(1000); // La tabla aún existe, esperar
            } catch (ResourceNotFoundException e) {
                return; // Tabla eliminada
            }
        }
    }

    private void insertTestData() {
        // Limpiar datos existentes e insertar datos de prueba
        userTable.putItem(UserDynamoEntity.builder()
                .username("testuser")
                .password("encoded_password")
                .roles(List.of("USER", "ADMIN"))
                .build());
                
        userTable.putItem(UserDynamoEntity.builder()
                .username("simpleuser")
                .password("simple_password")
                .roles(List.of("USER"))
                .build());
    }

    @Test
    @Order(1)
    void whenUserExists_thenReturnsUserWithCorrectRoles() {
        Optional<UserPrincipal> result = userRepositoryPort.apply("testuser");

        assertTrue(result.isPresent(), "User should be found");
        UserPrincipal user = result.get();
        assertEquals("testuser", user.getUsername());
        assertEquals("encoded_password", user.getPassword());
        assertIterableEquals(List.of("USER", "ADMIN"), user.getRoles());
    }

    @Test
    @Order(2)
    void whenUserDoesNotExist_thenReturnsEmptyOptional() {
        Optional<UserPrincipal> result = userRepositoryPort.apply("nonexistentuser");
        assertFalse(result.isPresent(), "Non-existent user should return empty");
    }

    @Test
    @Order(3)
    void whenUserExistsWithSingleRole_thenReturnsCorrectUser() {
        Optional<UserPrincipal> result = userRepositoryPort.apply("simpleuser");

        assertTrue(result.isPresent(), "Simple user should be found");
        UserPrincipal user = result.get();
        assertEquals("simpleuser", user.getUsername());
        assertEquals("simple_password", user.getPassword());
        assertIterableEquals(List.of("USER"), user.getRoles());
    }

    @Test
    @Order(4)
    void whenUserHasNoRoles_thenReturnsEmptyRolesList() {
        // Insertar usuario sin roles
        userTable.putItem(UserDynamoEntity.builder()
                .username("noroleuser")
                .password("password")
                .roles(null)
                .build());

        Optional<UserPrincipal> result = userRepositoryPort.apply("noroleuser");

        assertTrue(result.isPresent(), "User without roles should be found");
        UserPrincipal user = result.get();
        assertEquals("noroleuser", user.getUsername());
        assertTrue(user.getRoles().isEmpty(), "Roles should be empty list, not null");
    }
}