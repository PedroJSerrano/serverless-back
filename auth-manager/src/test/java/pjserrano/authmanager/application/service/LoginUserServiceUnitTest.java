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

    // Dependencias de nuestro caso de uso, las vamos a mockear
    @Mock
    private UserRepositoryPort mockUserRepositoryPort;

    @Mock
    private TokenServicePort mockTokenServicePort;

    // El sistema bajo prueba (función de login)
    private LoginUserUseCase loginUserUseCase;

    /* Este method se ejecuta antes de cada test. Aquí
    inicializamos nuestro caso de uso (la lambda), inyectándole los mocks.*/
    @BeforeEach
    void setUp() {
        LoginUserService loginUserService = new LoginUserService();
        this.loginUserUseCase = loginUserService.loginUser(mockUserRepositoryPort, mockTokenServicePort);
    }

    // Verifica el flujo exitoso de autenticación con credenciales válidas
    @Test
    void whenValidCredentials_thenShouldReturnUserSessionWithToken() {
        // GIVEN - Preparamos el escenario
        String username = "testuser";
        String password = "password123";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, List.of("ROLE_USER"));
        String expectedToken = "mocked.jwt.token";

        /* Le decimos a nuestro mock del repositorio qué debe hacer
        cuando se le llame con "testuser". Debe devolver un Optional.of(user).*/
        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));

        /* Le decimos a nuestro mock del servicio de tokens qué debe hacer
        cuando se le llame con el User. Debe devolver nuestro token de mock.*/
        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        // WHEN - Ejecutamos la acción que queremos probar
        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        // THEN - Verificamos los resultados
        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());

        /* Verificamos que los métodos de nuestros mocks fueron llamados
        exactamente una vez, asegurando que la lógica siguió el camino correcto.*/
        verify(mockUserRepositoryPort, times(1)).apply(username);
        verify(mockTokenServicePort, times(1)).apply(user);
    }

    // Verifica que se lanza excepción cuando la contraseña es incorrecta
    @Test
    void whenInvalidPassword_thenShouldThrowException() {
        // GIVEN
        String username = "testuser";
        String validPassword = "password123";
        String invalidPassword = "wrongpassword";
        ValidateUserCommand credentials = new ValidateUserCommand(username, invalidPassword);
        User user = new User(username, validPassword, List.of("ROLE_USER"));

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));

        // WHEN & THEN - Usamos assertThrows para verificar que se lanza la excepción
        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));

        // Verificamos que el tokenService no fue llamado
        verify(mockTokenServicePort, never()).apply(any());
    }

    // Verifica que se lanza excepción cuando el usuario no existe
    @Test
    void whenUserNotFound_thenShouldThrowException() {
        // GIVEN
        String username = "nonexistent";
        ValidateUserCommand credentials = new ValidateUserCommand(username, "anypassword");

        // El mock devuelve un Optional vacío, simulando que el usuario no existe
        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));
        
        // Verificamos que el tokenService no fue llamado
        verify(mockTokenServicePort, never()).apply(any());
    }

    // Verifica que se manejan correctamente las credenciales nulas
    @Test
    void whenNullCredentials_thenShouldThrowException() {
        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> loginUserUseCase.apply(null));
    }

    // Verifica que se manejan correctamente los usuarios con roles múltiples
    @Test
    void whenUserWithMultipleRoles_thenShouldReturnValidSession() {
        // GIVEN
        String username = "adminuser";
        String password = "adminpass";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, List.of("ROLE_USER", "ROLE_ADMIN"));
        String expectedToken = "admin.jwt.token";

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));
        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        // WHEN
        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        // THEN
        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());
    }

    // Verifica que se manejan correctamente los usuarios sin roles
    @Test
    void whenUserWithNoRoles_thenShouldReturnValidSession() {
        // GIVEN
        String username = "basicuser";
        String password = "basicpass";
        ValidateUserCommand credentials = new ValidateUserCommand(username, password);
        User user = new User(username, password, List.of());
        String expectedToken = "basic.jwt.token";

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(user));
        when(mockTokenServicePort.apply(user)).thenReturn(expectedToken);

        // WHEN
        ValidateUserResponse result = loginUserUseCase.apply(credentials);

        // THEN
        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());
    }
}