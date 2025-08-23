package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {DynamoDbConfig.class})
class DynamoDbConfigUnitTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testDynamoDbClientsCreation() {
        assertNotNull(context, "El contexto de Spring no se cargó.");

        DynamoDbClient dynamoDbClient = context.getBean(DynamoDbClient.class);
        assertNotNull(dynamoDbClient, "El bean DynamoDbClient no se creó correctamente.");

        DynamoDbEnhancedClient enhancedClient = context.getBean(DynamoDbEnhancedClient.class);
        assertNotNull(enhancedClient, "El bean DynamoDbEnhancedClient no se creó correctamente.");
    }
}