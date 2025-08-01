package pjserrano.login.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = {DynamoDbConfig.class})
@Import(DynamoDBUserRepositoryAdapter.class)
class DynamoDBUserRepositoryAdapterTest {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");

    @Container
    public static LocalStackContainer localstack =
            new LocalStackContainer(LOCALSTACK_IMAGE)
                    .withServices(LocalStackContainer.Service.DYNAMODB);

    @DynamicPropertySource
    static void registerDynamoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.dynamodb.endpoint", () -> localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
        registry.add("aws.region", () -> localstack.getRegion());
        registry.add("aws.accessKeyId", () -> localstack.getAccessKey());
        registry.add("aws.secretAccessKey", () -> localstack.getSecretKey());
    }

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbTable<UserDynamoEntity> userTable;

    @BeforeEach
    void createTableAndInsertData() {
        try {
            // El metodo createTable() es asincrono
            userTable.createTable();

            // Esperamos a que la tabla esté activa antes de continuar
            // Es crucial para evitar que el test falle por un problema de sincronización

        } catch (Exception e) {
            // Si la tabla ya existe, la eliminamos y volvemos a crearla para garantizar un estado limpio
            userTable.deleteTable();
            userTable.createTable();
        }

        userTable.putItem(UserDynamoEntity.builder()
                .username("testuser")
                .password("encoded_password")
                .roles(List.of("USER", "ADMIN"))
                .build());
    }

    @Test
    void whenUserExists_thenReturnsUserWithCorrectRoles() {
        Optional<UserPrincipal> result = userRepositoryPort.apply("testuser");

        assertTrue(result.isPresent());
        UserPrincipal user = result.get();
        assertEquals("testuser", user.getUsername());
        assertEquals("encoded_password", user.getPassword());
        assertIterableEquals(List.of("USER", "ADMIN"), user.getRoles());
    }

    @Test
    void whenUserDoesNotExist_thenReturnsEmptyOptional() {
        Optional<UserPrincipal> result = userRepositoryPort.apply("nonexistentuser");
        assertFalse(result.isPresent());
    }
}