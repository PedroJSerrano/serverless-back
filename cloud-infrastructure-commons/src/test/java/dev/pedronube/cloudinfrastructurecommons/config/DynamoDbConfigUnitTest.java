package dev.pedronube.cloudinfrastructurecommons.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {DynamoDbConfig.class, TestDynamoDbConfig.class})
class DynamoDbConfigUnitTest {

    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DynamoDbClient dynamoDbClient() {
            return DynamoDbClient.builder()
                    .region(software.amazon.awssdk.regions.Region.EU_WEST_1)
                    .credentialsProvider(software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create("test", "test")))
                    .build();
        }

        @Bean
        public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
            return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient()).build();
        }
    }

    @Test
    void testDynamoDbClientsCreation() {
        assertNotNull(context, "El contexto de Spring no se cargó.");

        DynamoDbClient dynamoDbClient = context.getBean(DynamoDbClient.class);
        assertNotNull(dynamoDbClient, "El bean DynamoDbClient no se creó correctamente.");

        DynamoDbEnhancedClient enhancedClient = context.getBean(DynamoDbEnhancedClient.class);
        assertNotNull(enhancedClient, "El bean DynamoDbEnhancedClient no se creó correctamente.");
    }
}
