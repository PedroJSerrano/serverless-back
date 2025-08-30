package pjserrano.authmanager.infrastructure.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
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

    private Function<LoginRequest, ResponseEntity<LoginResponse>> loginFunction;

    @BeforeEach
    void setUp() {
        LoginApiFunctions loginApiFunctions = new LoginApiFunctions(mockLoginUserUseCase);
        this.loginFunction = loginApiFunctions.login();
    }

    @Test
    void givenValidRequest_whenLoginFunctionCalled_thenReturnsLoginResponse() {
        String username = "user@example.com";
        String password = "password123";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String jwtToken = "mock-jwt-token-12345";
        ValidateUserResponse userSession = new ValidateUserResponse("mock-user-id", jwtToken);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(userSession);

        ResponseEntity<LoginResponse> response = loginFunction.apply(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(jwtToken, response.getBody().getJwtToken(), "El token del JWT debería coincidir");
        assertEquals(username, response.getBody().getUserId());
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand(username, password));
    }

    @Test
    void givenInvalidCredentials_whenLoginFunctionCalled_thenThrowsException() {
        LoginRequest loginRequest = new LoginRequest("invalid@user.com", "wrongpassword");

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(any(ValidateUserCommand.class));
    }

    @Test
    void givenRequestWithNullFields_whenLoginFunctionCalled_thenHandlesGracefully() {
        LoginRequest loginRequest = new LoginRequest(null, null);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand(null, null));
    }

    @Test
    void givenUserSession_whenMappingToResponse_thenExtractsTokenCorrectly() {
        String username = "testuser";
        String password = "testpass";
        LoginRequest loginRequest = new LoginRequest(username, password);

        String expectedToken = "expected-jwt-token";
        ValidateUserResponse userSession = new ValidateUserResponse("user123", expectedToken);

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class))).thenReturn(userSession);

        ResponseEntity<LoginResponse> response = loginFunction.apply(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().getJwtToken());
    }

    @Test
    void givenEmptyCredentials_whenLoginFunctionCalled_thenPassesToUseCase() {
        LoginRequest loginRequest = new LoginRequest("", "");

        when(mockLoginUserUseCase.apply(any(ValidateUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        assertThrows(InvalidCredentialsException.class, () -> loginFunction.apply(loginRequest));
        verify(mockLoginUserUseCase).apply(new ValidateUserCommand("", ""));
    }
}