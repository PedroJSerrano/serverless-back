package pjserrano.login.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.ApplicationContext;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.application.port.out.TokenServicePort;
import pjserrano.login.application.port.out.UserRepositoryPort;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración que verifican la configuración Spring.
 * Prueban que el bean se crea correctamente en el contexto.
 */
@SpringBootTest(classes = {LoginUserService.class, LoginUserServiceIntegrationTest.TestConfig.class})
class LoginUserServiceIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public UserRepositoryPort userRepositoryPort() {
            return org.mockito.Mockito.mock(UserRepositoryPort.class);
        }
        
        @Bean
        @Primary
        public TokenServicePort tokenServicePort() {
            return org.mockito.Mockito.mock(TokenServicePort.class);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que el bean LoginUserUseCase está registrado en Spring
    @Test
    void loginUserUseCaseBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("loginUser"));
        LoginUserUseCase useCase = applicationContext.getBean(LoginUserUseCase.class);
        assertNotNull(useCase);
    }

    // Verifica que la clase de configuración está cargada
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("loginUserService"));
        Object configBean = applicationContext.getBean("loginUserService");
        assertTrue(configBean instanceof LoginUserService, "Bean should be instance of LoginUserService");
    }

    // Verifica que el bean es singleton
    @Test
    void loginUserUseCaseIsSingleton() {
        LoginUserUseCase useCase1 = applicationContext.getBean(LoginUserUseCase.class);
        LoginUserUseCase useCase2 = applicationContext.getBean(LoginUserUseCase.class);
        assertSame(useCase1, useCase2, "LoginUserUseCase should be singleton");
    }
}