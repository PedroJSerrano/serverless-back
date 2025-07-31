package pjserrano.login.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.domain.UserCredentials;
import pjserrano.login.domain.UserSession;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LoginApiFunctions.class)
class LoginApiFunctionsTest {

    @Autowired
    private Function<LoginRequest, LoginResponse> loginFunction;

    @MockBean
    private LoginUserUseCase loginUserUseCase;

    @Test
    void givenValidRequest_whenLoginFunctionCalled_thenReturnsLoginResponse() {
        // GIVEN:
        String username = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Creamos el objeto UserSession que el caso de uso devolvería
        String jwtToken = "mock-jwt-token-12345";
        UserSession userSession = new UserSession("mock-user-id", jwtToken);

        // Configura el comportamiento del mock.
        // Cuando se llame a loginUserUseCase.apply(), devolverá el UserSession que hemos creado.
        when(loginUserUseCase.apply(any(UserCredentials.class)))
                .thenReturn(userSession);

        // WHEN:
        // Llamamos al método apply() de la función que queremos testear.
        LoginResponse response = loginFunction.apply(loginRequest);

        // THEN:
        // 1. Verificamos que el resultado del DTO de salida sea el esperado.
        assertEquals(jwtToken, response.getJwtToken(), "El token del JWT debería coincidir");

        // 2. Verificamos que el caso de uso fue invocado una vez con las credenciales correctas.
        verify(loginUserUseCase).apply(new UserCredentials(username, password));
    }
}