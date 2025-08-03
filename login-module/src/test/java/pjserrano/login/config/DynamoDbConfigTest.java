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

        @Test
        void contextLoadsAndClientsAreAutoConfiguredForLocalStack() {
            assertNotNull(dynamoDbClient, "DynamoDbClient should be auto-configured by Spring Cloud AWS");
            assertNotNull(dynamoDbEnhancedClient, "DynamoDbEnhancedClient should be auto-configured by Spring Cloud AWS");
            logger.info("LocalStack test passed - clients auto-configured successfully");
        }
    }

    @Nested
    @SpringBootTest(classes = DynamoDbConfig.class)
    class ProductionTest {

        @Autowired
        private DynamoDbClient dynamoDbClient;

        @Autowired
        private DynamoDbEnhancedClient dynamoDbEnhancedClient;

        @Test
        void contextLoadsAndClientsAreAutoConfiguredForProduction() {
            assertNotNull(dynamoDbClient, "DynamoDbClient should be auto-configured by Spring Cloud AWS");
            assertNotNull(dynamoDbEnhancedClient, "DynamoDbEnhancedClient should be auto-configured by Spring Cloud AWS");
            logger.info("Production test passed - clients auto-configured with default AWS settings");
        }
    }
}
