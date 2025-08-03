package pjserrano.login.infrastructure.adapter.out.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.ApplicationContext;
import pjserrano.login.application.port.out.JwtSecretProviderPort;
import pjserrano.login.application.port.out.TokenServicePort;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración que verifican la configuración Spring.
 * Prueban que el bean se crea correctamente en el contexto.
 */
@SpringBootTest(classes = {SecurityAdapterConfig.class, SecurityAdapterConfigIntegrationTest.TestConfig.class}, properties = {"JWT_EXPIRATION=3600000"})
class SecurityAdapterConfigIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public JwtSecretProviderPort jwtSecretProviderPort() {
            return org.mockito.Mockito.mock(JwtSecretProviderPort.class);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que el bean jwtTokenServiceAdapter está registrado en Spring
    @Test
    void jwtTokenServiceAdapterBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("jwtTokenServiceAdapter"));
        TokenServicePort tokenService = applicationContext.getBean(TokenServicePort.class);
        assertNotNull(tokenService);
    }

    // Verifica que la clase de configuración está cargada
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("securityAdapterConfig"));
        Object configBean = applicationContext.getBean("securityAdapterConfig");
        assertTrue(configBean instanceof SecurityAdapterConfig, "Bean should be instance of SecurityAdapterConfig");
    }

    // Verifica que el bean es singleton
    @Test
    void jwtTokenServiceAdapterIsSingleton() {
        TokenServicePort tokenService1 = applicationContext.getBean(TokenServicePort.class);
        TokenServicePort tokenService2 = applicationContext.getBean(TokenServicePort.class);
        assertSame(tokenService1, tokenService2, "jwtTokenServiceAdapter should be singleton");
    }

    // Verifica que el TokenServicePort se puede obtener por tipo
    @Test
    void tokenServicePortCanBeRetrievedByType() {
        TokenServicePort tokenService = applicationContext.getBean(TokenServicePort.class);
        assertNotNull(tokenService);
    }

    // Verifica que SecurityAdapterConfig se puede obtener por tipo
    @Test
    void securityAdapterConfigCanBeRetrievedByType() {
        SecurityAdapterConfig config = applicationContext.getBean(SecurityAdapterConfig.class);
        assertNotNull(config);
    }

    // Verifica que el bean se puede obtener por nombre específico
    @Test
    void jwtTokenServiceAdapterCanBeRetrievedByName() {
        Object bean = applicationContext.getBean("jwtTokenServiceAdapter");
        assertNotNull(bean);
        assertTrue(bean instanceof TokenServicePort);
    }
}