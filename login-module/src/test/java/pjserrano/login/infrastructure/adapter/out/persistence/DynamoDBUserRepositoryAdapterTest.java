package pjserrano.login.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeAll;
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
import pjserrano.login.config.DynamoDBConfig;
import pjserrano.login.domain.UserPrincipal;
import pjserrano.login.infrastructure.adapter.out.persistence.model.UserDynamoEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = {DynamoDBConfig.class})
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

    private static DynamoDbEnhancedClient enhancedClient;
    private static DynamoDbTable<UserDynamoEntity> userTable;

    @BeforeAll
    static void setup() {
        DynamoDbClient ddbClient = DynamoDbClient.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .region(Region.of(localstack.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build();
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddbClient)
                .build();
        userTable = enhancedClient.table("users", TableSchema.fromBean(UserDynamoEntity.class));
    }

    @BeforeEach
    void createTableAndInsertData() {
        try {
            userTable.createTable();
        } catch (Exception e) {
            // Ignoramos si la tabla ya existe
        }

        userTable.putItem(UserDynamoEntity.builder()
                .username("testuser")
                .password("encoded_password")
                .roles(Set.of("USER", "ADMIN"))
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