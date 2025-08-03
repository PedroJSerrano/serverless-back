package pjserrano.login.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de producción que usan configuración AWS por defecto.
 * Verifica comportamiento sin LocalStack, usando credenciales reales.
 */
@SpringBootTest(classes = DynamoDbConfig.class)
class DynamoDbConfigTest {

    static Logger logger = Logger.getLogger(DynamoDbConfigTest.class.getName());

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que la configuración funciona en entorno de producción
    @Test
    void contextLoadsAndClientsAreAutoConfiguredForProduction() {
        assertNotNull(dynamoDbClient, "DynamoDbClient should be auto-configured by Spring Cloud AWS");
        assertNotNull(dynamoDbEnhancedClient, "DynamoDbEnhancedClient should be auto-configured by Spring Cloud AWS");
        logger.info("Production test passed - clients auto-configured with default AWS settings");
    }

    // Confirma que los beans son singletons (una sola instancia por contexto)
    @Test
    void beansAreSingletons() {
        DynamoDbClient client1 = applicationContext.getBean(DynamoDbClient.class);
        DynamoDbClient client2 = applicationContext.getBean(DynamoDbClient.class);
        assertSame(client1, client2, "DynamoDbClient should be singleton");

        DynamoDbEnhancedClient enhanced1 = applicationContext.getBean(DynamoDbEnhancedClient.class);
        DynamoDbEnhancedClient enhanced2 = applicationContext.getBean(DynamoDbEnhancedClient.class);
        assertSame(enhanced1, enhanced2, "DynamoDbEnhancedClient should be singleton");
    }

    // Verifica que la clase de configuración está cargada como bean
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("dynamoDbConfig"));
        Object configBean = applicationContext.getBean("dynamoDbConfig");
        assertTrue(configBean instanceof DynamoDbConfig, "Bean should be instance of DynamoDbConfig");
    }
}
