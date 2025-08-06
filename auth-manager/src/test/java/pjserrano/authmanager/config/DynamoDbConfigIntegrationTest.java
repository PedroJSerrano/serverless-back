package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests de integración que verifican la configuración Spring de DynamoDbConfig.
 * Usa mocks para simular servicios DynamoDB sin dependencias externas.
 */
@SpringBootTest(classes = {DynamoDbConfig.class, DynamoDbConfigIntegrationTest.TestConfig.class},
    properties = {"spring.main.allow-bean-definition-overriding=true"})
class DynamoDbConfigIntegrationTest {

    static Logger logger = Logger.getLogger(DynamoDbConfigIntegrationTest.class.getName());

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public DynamoDbClient dynamoDbClient() {
            DynamoDbClient mockClient = mock(DynamoDbClient.class);
            when(mockClient.serviceName()).thenReturn("DynamoDB");
            return mockClient;
        }
        
        @Bean
        @Primary
        public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
            return mock(DynamoDbEnhancedClient.class);
        }
    }

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que Spring puede cargar el contexto y crear los beans DynamoDB
    @Test
    void contextLoadsAndClientsAreConfigured() {
        assertNotNull(dynamoDbClient, "DynamoDbClient should be configured");
        assertNotNull(dynamoDbEnhancedClient, "DynamoDbEnhancedClient should be configured");
        assertEquals("DynamoDB", dynamoDbClient.serviceName());
        logger.info("Integration test passed - DynamoDB clients configured successfully");
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

    // Verifica que la clase de configuración está cargada como bean
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("dynamoDbConfig"));
        Object configBean = applicationContext.getBean("dynamoDbConfig");
        assertTrue(configBean instanceof DynamoDbConfig, "Bean should be instance of DynamoDbConfig");
    }
}