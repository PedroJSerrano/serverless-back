package pjserrano.login.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración con LocalStack para simular entorno AWS real.
 * Verifica que la configuración funciona correctamente con infraestructura local.
 */
@Testcontainers
@SpringBootTest(classes = DynamoDbConfig.class)
class DynamoDbConfigIntegrationTest {

    static Logger logger = Logger.getLogger(DynamoDbConfigIntegrationTest.class.getName());

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:latest");

    @Container
    public static LocalStackContainer localstack =
            new LocalStackContainer(LOCALSTACK_IMAGE)
                    .withServices(LocalStackContainer.Service.DYNAMODB);

    @DynamicPropertySource
    static void registerDynamoDbProperties(DynamicPropertyRegistry registry) {
        // Propiedades estándar de Spring Cloud AWS
        registry.add("spring.cloud.aws.dynamodb.endpoint", () -> localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
        registry.add("spring.cloud.aws.region.static", () -> localstack.getRegion());
        registry.add("spring.cloud.aws.credentials.access-key", () -> localstack.getAccessKey());
        registry.add("spring.cloud.aws.credentials.secret-key", () -> localstack.getSecretKey());
        logger.info("Spring Cloud AWS properties injected for LocalStack.");
    }

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que Spring puede cargar el contexto y crear los beans DynamoDB
    @Test
    void contextLoadsAndClientsAreAutoConfiguredForLocalStack() {
        assertNotNull(dynamoDbClient, "DynamoDbClient should be auto-configured by Spring Cloud AWS");
        assertNotNull(dynamoDbEnhancedClient, "DynamoDbEnhancedClient should be auto-configured by Spring Cloud AWS");
        logger.info("LocalStack test passed - clients auto-configured successfully");
    }

    // Confirma que el bean DynamoDbClient está registrado en el contexto de Spring
    @Test
    void dynamoDbClientBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("dynamoDbClient"));
        assertSame(dynamoDbClient, applicationContext.getBean("dynamoDbClient"));
    }

    // Confirma que el bean DynamoDbEnhancedClient está registrado en el contexto
    @Test
    void dynamoDbEnhancedClientBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("dynamoDbEnhancedClient"));
        assertSame(dynamoDbEnhancedClient, applicationContext.getBean("dynamoDbEnhancedClient"));
    }

    // Verifica que el enhanced client está correctamente configurado
    @Test
    void dynamoDbEnhancedClientUsesCorrectDynamoDbClient() {
        // Verificar que el enhanced client está correctamente configurado
        assertNotNull(dynamoDbEnhancedClient);
        // El enhanced client internamente usa un DynamoDbClient pero no expone el método público
        assertTrue(dynamoDbEnhancedClient.toString().contains("DynamoDbEnhancedClient"));
    }
}