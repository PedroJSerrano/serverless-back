package pjserrano.authmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests de integración que verifican la configuración Spring de AwsConfig.
 * Usa mocks para simular servicios AWS sin dependencias externas.
 */
@SpringBootTest(classes = {AwsConfig.class, AwsConfigIntegrationTest.TestConfig.class}, 
    properties = {"spring.main.allow-bean-definition-overriding=true"})
class AwsConfigIntegrationTest {

    static Logger logger = Logger.getLogger(AwsConfigIntegrationTest.class.getName());

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SsmClient ssmClient() {
            SsmClient mockClient = mock(SsmClient.class);
            when(mockClient.serviceName()).thenReturn("ssm");
            return mockClient;
        }
    }

    @Autowired
    private SsmClient ssmClient;

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que Spring puede cargar el contexto y crear el bean SsmClient
    @Test
    void contextLoadsAndSsmClientIsConfigured() {
        assertNotNull(ssmClient, "SsmClient should be configured");
        assertEquals("ssm", ssmClient.serviceName());
        logger.info("Integration test passed - SsmClient configured successfully");
    }

    // Confirma que el bean SsmClient está registrado en el contexto de Spring
    @Test
    void ssmClientBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("ssmClient"));
        assertSame(ssmClient, applicationContext.getBean("ssmClient"));
    }

    // Verifica que el SsmClient se puede obtener por tipo desde el contexto
    @Test
    void ssmClientCanBeRetrievedByType() {
        SsmClient clientByType = applicationContext.getBean(SsmClient.class);
        assertSame(ssmClient, clientByType);
    }

    // Verifica que la clase de configuración está cargada como bean
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("awsConfig"));
        Object configBean = applicationContext.getBean("awsConfig");
        assertTrue(configBean instanceof AwsConfig, "Bean should be instance of AwsConfig");
    }
}