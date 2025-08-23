package pjserrano.authmanager.infrastructure.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.authmanager.domain.port.dto.ValidateUserResponse;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.domain.port.dto.ValidateUserCommand;
import pjserrano.authmanager.domain.exceptions.InvalidCredentialsException;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginRequest;
import pjserrano.authmanager.infrastructure.adapter.in.web.dto.LoginResponse;

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
        ValidateUserResponse userSession = new ValidateUserResponse("mock-user-id", jwtToken);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(userSession);

        // WHEN
        LoginResponse response = loginFunction.apply(loginRequest);

        // THEN
        assertEquals(jwtToken, response.getJwtToken(), "El token del JWT debería coincidir");
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand(username, password));
    }

    // Verifica que se propagan las excepciones del caso de uso
    @Test
    void givenInvalidCredentials_whenLoginFunctionCalled_thenThrowsException() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("invalid@user.com", "wrongpassword");

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(any(ValidateUserCommand.class));
    }

    // Verifica el manejo de requests con campos nulos
    @Test
    void givenRequestWithNullFields_whenLoginFunctionCalled_thenHandlesGracefully() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest(null, null);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand(null, null));
    }

    // Verifica el mapeo correcto de ValidateUserResponse a LoginResponse
    @Test
    void givenUserSession_whenMappingToResponse_thenExtractsTokenCorrectly() {
        // GIVEN
        String username = "testuser";
        String password = "testpass";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String expectedToken = "expected-jwt-token";
        ValidateUserResponse userSession = new ValidateUserResponse("user123", expectedToken);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(userSession);

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

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand("", ""));
    }
}