package pjserrano.login.config;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DynamoDbConfigTest {

    static Logger logger = Logger.getLogger(DynamoDbConfigTest.class.getName());

    @Nested
    @Testcontainers
    @SpringBootTest(classes = DynamoDbConfig.class)
    class LocalstackTest {

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
            logger.info("LocalStack properties injected into Spring context.");
        }

        @Autowired
        private DynamoDbClient dynamoDbClient;

        @Autowired
        private DynamoDbEnhancedClient dynamoDbEnhancedClient;

        @Test
        void contextLoadsAndClientsAreConfiguredForLocalStack() {
            assertNotNull(dynamoDbClient);
            assertNotNull(dynamoDbEnhancedClient);
        }
    }

    @Nested
    @SpringBootTest(classes = DynamoDbConfig.class) // Se han eliminado las propiedades para que Spring inyecte 'null'
    class ProductionTest {

        @Autowired
        private DynamoDbClient dynamoDbClient;

        @Autowired
        private DynamoDbEnhancedClient dynamoDbEnhancedClient;

        @Test
        void contextLoadsAndClientsAreConfiguredForProductionDefaults() {
            assertNotNull(dynamoDbClient);
            assertNotNull(dynamoDbEnhancedClient);
        }
    }
}
