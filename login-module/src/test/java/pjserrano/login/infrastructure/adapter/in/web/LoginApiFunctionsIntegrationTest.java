package pjserrano.login.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.ApplicationContext;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración que verifican la configuración Spring.
 * Prueban que el bean se crea correctamente en el contexto.
 */
@SpringBootTest(classes = {LoginApiFunctions.class, LoginApiFunctionsIntegrationTest.TestConfig.class})
class LoginApiFunctionsIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public LoginUserUseCase loginUserUseCase() {
            return org.mockito.Mockito.mock(LoginUserUseCase.class);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    // Verifica que el bean loginFunction está registrado en Spring
    @Test
    void loginFunctionBeanIsRegistered() {
        assertTrue(applicationContext.containsBean("loginFunction"));
        @SuppressWarnings("unchecked")
        Function<LoginRequest, LoginResponse> function = 
            (Function<LoginRequest, LoginResponse>) applicationContext.getBean("loginFunction");
        assertNotNull(function);
    }

    // Verifica que la clase de configuración está cargada
    @Test
    void configurationClassIsLoaded() {
        assertTrue(applicationContext.containsBean("loginApiFunctions"));
        Object configBean = applicationContext.getBean("loginApiFunctions");
        assertTrue(configBean instanceof LoginApiFunctions, "Bean should be instance of LoginApiFunctions");
    }

    // Verifica que el bean es singleton
    @Test
    void loginFunctionIsSingleton() {
        @SuppressWarnings("unchecked")
        Function<LoginRequest, LoginResponse> function1 = 
            (Function<LoginRequest, LoginResponse>) applicationContext.getBean("loginFunction");
        @SuppressWarnings("unchecked")
        Function<LoginRequest, LoginResponse> function2 = 
            (Function<LoginRequest, LoginResponse>) applicationContext.getBean("loginFunction");
        assertSame(function1, function2, "loginFunction should be singleton");
    }

    // Verifica que LoginApiFunctions se puede obtener por tipo
    @Test
    void loginApiFunctionsCanBeRetrievedByType() {
        LoginApiFunctions config = applicationContext.getBean(LoginApiFunctions.class);
        assertNotNull(config);
    }
}