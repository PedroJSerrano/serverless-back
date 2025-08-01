package pjserrano.login.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // <-- Importación importante
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

    // Con @MockBean, Spring crea el mock por y lo inyecta
    @MockBean
    private LoginUserUseCase loginUserUseCase;

    @Test
    void givenValidRequest_whenLoginFunctionCalled_thenReturnsLoginResponse() {
        // GIVEN:
        String username = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String jwtToken = "mock-jwt-token-12345";
        UserSession userSession = new UserSession("mock-user-id", jwtToken);

        // Configura el comportamiento del mock. El method es el mismo
        when(loginUserUseCase.apply(any(UserCredentials.class)))
                .thenReturn(userSession);

        // WHEN:
        LoginResponse response = loginFunction.apply(loginRequest);

        // THEN:
        assertEquals(jwtToken, response.getJwtToken(), "El token del JWT debería coincidir");

        // Verificamos que el caso de uso fue invocado una vez con las credenciales correctas.
        verify(loginUserUseCase).apply(new UserCredentials(username, password));
    }
}