package pjserrano.authmanager.infrastructure.adapter.out.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.ApplicationContext;
import pjserrano.authmanager.application.port.out.JwtSecretProviderPort;
import software.amazon.awssdk.services.ssm.SsmClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración que verifican la configuración Spring.
 * Prueban que el bean se crea correctamente en el contexto.
 */
@SpringBootTest(classes = {SsmJwtSecretProviderAdapter.class, SsmJwtSecretProviderAdapterIntegrationTest.TestConfig.class})
class SsmJwtSecretProviderAdapterIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SsmClient ssmClient() {
            return org.mockito.Mockito.mock(SsmClient.class);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que el bean jwtSecretProvider está registrado en Spring
    @Test
    void jwtSecretProviderBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("jwtSecretProvider"));
        JwtSecretProviderPort secretProvider = applicationContext.getBean(JwtSecretProviderPort.class);
        assertNotNull(secretProvider);
    }

    // Verifica que la clase de configuración está cargada
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("ssmJwtSecretProviderAdapter"));
        Object configBean = applicationContext.getBean("ssmJwtSecretProviderAdapter");
        assertTrue(configBean instanceof SsmJwtSecretProviderAdapter, "Bean should be instance of SsmJwtSecretProviderAdapter");
    }

    // Verifica que el bean es singleton
    @Test
    void jwtSecretProviderIsSingleton() {
        JwtSecretProviderPort provider1 = applicationContext.getBean(JwtSecretProviderPort.class);
        JwtSecretProviderPort provider2 = applicationContext.getBean(JwtSecretProviderPort.class);
        assertSame(provider1, provider2, "jwtSecretProvider should be singleton");
    }

    // Verifica que el JwtSecretProviderPort se puede obtener por tipo
    @Test
    void jwtSecretProviderPortCanBeRetrievedByType() {
        JwtSecretProviderPort secretProvider = applicationContext.getBean(JwtSecretProviderPort.class);
        assertNotNull(secretProvider);
    }

    // Verifica que SsmJwtSecretProviderAdapter se puede obtener por tipo
    @Test
    void ssmJwtSecretProviderAdapterCanBeRetrievedByType() {
        SsmJwtSecretProviderAdapter adapter = applicationContext.getBean(SsmJwtSecretProviderAdapter.class);
        assertNotNull(adapter);
    }

    // Verifica que el bean se puede obtener por nombre específico
    @Test
    void jwtSecretProviderCanBeRetrievedByName() {
        Object bean = applicationContext.getBean("jwtSecretProvider");
        assertNotNull(bean);
        assertTrue(bean instanceof JwtSecretProviderPort);
    }

    // Verifica que el SsmClient está disponible como dependencia
    @Test
    void ssmClientDependencyIsAvailable() {
        assertTrue(applicationContext.containsBean("ssmClient"));
        SsmClient client = applicationContext.getBean(SsmClient.class);
        assertNotNull(client);
    }
}