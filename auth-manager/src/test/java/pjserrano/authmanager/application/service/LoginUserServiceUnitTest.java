package pjserrano.authmanager.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.authmanager.domain.model.User;
import pjserrano.authmanager.domain.port.dto.ValidateUserResponse;
import pjserrano.authmanager.domain.port.in.LoginUserUseCase;
import pjserrano.authmanager.domain.port.out.TokenServicePort;
import pjserrano.authmanager.domain.port.out.UserRepositoryPort;
import pjserrano.authmanager.domain.port.dto.ValidateUserCommand;
import pjserrano.authmanager.domain.exceptions.InvalidCredentialsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios con mocks para verificar la lógica de negocio.
 * Prueban el comportamiento del servicio de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class LoginUserServiceUnitTest {
    @Mock
    private UserRepositoryPort mockUserRepositoryPort;

    @Mock
    private TokenServicePort mockTokenServicePort;

    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp() {
        LoginUserService loginUserService = new LoginUserService();
        this.loginUserUseCase = loginUserService.loginUser(mockUserRepositoryPort, mockTokenServicePort);
    }

    @Test
    void whenValidCredentials_thenShouldReturnUserSessionWithToken() {
        String username = "testuser";
        String password = "password123";
        String email = "testuser@mail.com";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, email, List.of("ROLE_USER"));
        String expectedToken = "mocked.jwt.token";

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));

        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());

        verify(mockUserRepositoryPort, times(1)).apply(username);
        verify(mockTokenServicePort, times(1)).apply(user);
    }

    @Test
    void whenInvalidPassword_thenShouldThrowException() {
        String username = "testuser";
        String validPassword = "password123";
        String email = "testuser@mail.com";
        String invalidPassword = "wrongpassword";
        ValidateUserCommand credentials = new ValidateUserCommand(username, invalidPassword);
        User user = new User(username, validPassword, email, List.of("ROLE_USER"));

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));

        verify(mockTokenServicePort, never()).apply(any());
    }

    @Test
    void whenUserNotFound_thenShouldThrowException() {
        String username = "nonexistent";
        ValidateUserCommand credentials = new ValidateUserCommand(username, "anypassword");

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));

        verify(mockTokenServicePort, never()).apply(any());
    }

    @Test
    void whenNullCredentials_thenShouldThrowException() {
        assertThrows(NullPointerException.class, () -> loginUserUseCase.apply(null));
    }

    @Test
    void whenUserWithMultipleRoles_thenShouldReturnValidSession() {
        String username = "adminuser";
        String password = "adminpass";
        String email = "testuser@mail.com";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, email, List.of("ROLE_USER", "ROLE_ADMIN"));
        String expectedToken = "admin.jwt.token";

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));
        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());
    }

    @Test
    void whenUserWithNoRoles_thenShouldReturnValidSession() {
        String username = "basicuser";
        String password = "basicpass";
        String email = "testuser@mail.com";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, email, List.of());
        String expectedToken = "basic.jwt.token";

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));
        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());
    }
}