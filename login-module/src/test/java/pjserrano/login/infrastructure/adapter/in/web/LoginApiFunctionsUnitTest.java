package pjserrano.login.infrastructure.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.login.application.port.in.LoginUserUseCase;
import pjserrano.login.domain.UserCredentials;
import pjserrano.login.domain.UserSession;
import pjserrano.login.domain.exceptions.InvalidCredentialsException;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.login.infrastructure.adapter.in.web.dto.LoginResponse;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios con mocks para verificar la lógica del adaptador web.
 * Prueban el comportamiento de la función Lambda de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class LoginApiFunctionsUnitTest {

    @Mock
    private LoginUserUseCase mockLoginUserUseCase;

    private Function<LoginRequest, LoginResponse> loginFunction;

    @BeforeEach
    void setUp() {
        LoginApiFunctions loginApiFunctions = new LoginApiFunctions(mockLoginUserUseCase);
        this.loginFunction = loginApiFunctions.loginFunction();
    }

    // Verifica el flujo exitoso de login con credenciales válidas
    @Test
    void givenValidRequest_whenLoginFunctionCalled_thenReturnsLoginResponse() {
        // GIVEN
        String username = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String jwtToken = "mock-jwt-token-12345";
        UserSession userSession = new UserSession("mock-user-id", jwtToken);

        when(mockLoginUserUseCase.apply(any(UserCredentials.class))).thenReturn(userSession);

        // WHEN
        LoginResponse response = loginFunction.apply(loginRequest);

        // THEN
        assertEquals(jwtToken, response.getJwtToken(), "El token del JWT debería coincidir");
        verify(mockLoginUserUseCase).apply(new UserCredentials(username, password));
    }

    // Verifica que se propagan las excepciones del caso de uso
    @Test
    void givenInvalidCredentials_whenLoginFunctionCalled_thenThrowsException() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("invalid@user.com", "wrongpassword");

        when(mockLoginUserUseCase.apply(any(UserCredentials.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(any(UserCredentials.class));
    }

    // Verifica el manejo de requests con campos nulos
    @Test
    void givenRequestWithNullFields_whenLoginFunctionCalled_thenHandlesGracefully() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest(null, null);

        when(mockLoginUserUseCase.apply(any(UserCredentials.class)))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new UserCredentials(null, null));
    }

    // Verifica el mapeo correcto de UserSession a LoginResponse
    @Test
    void givenUserSession_whenMappingToResponse_thenExtractsTokenCorrectly() {
        // GIVEN
        String username = "testuser";
        String password = "testpass";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String expectedToken = "expected-jwt-token";
        UserSession userSession = new UserSession("user123", expectedToken);

        when(mockLoginUserUseCase.apply(any(UserCredentials.class))).thenReturn(userSession);

        // WHEN
        LoginResponse response = loginFunction.apply(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals(expectedToken, response.getJwtToken());
    }

    // Verifica que se manejan correctamente los requests vacíos
    @Test
    void givenEmptyCredentials_whenLoginFunctionCalled_thenPassesToUseCase() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("", "");

        when(mockLoginUserUseCase.apply(any(UserCredentials.class)))
                .thenThrow(new InvalidCredentialsException("Empty credentials"));

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new UserCredentials("", ""));
    }
}