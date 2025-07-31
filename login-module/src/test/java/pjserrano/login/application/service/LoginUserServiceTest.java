package pjserrano.login.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjserrano.login.application.port.out.TokenServicePort;
import pjserrano.login.application.port.out.UserRepositoryPort;
import pjserrano.login.domain.UserCredentials;
import pjserrano.login.domain.UserPrincipal;
import pjserrano.login.domain.UserSession;
import pjserrano.login.domain.exceptions.InvalidCredentialsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito para los tests de JUnit 5
class LoginUserServiceTest {

    // Dependencias de nuestro caso de uso, las vamos a mockear
    @Mock
    private UserRepositoryPort mockUserRepositoryPort;

    @Mock
    private TokenServicePort mockTokenServicePort;

    // El sistema bajo prueba (función de login)
    private Function<UserCredentials, UserSession> loginUserUseCase;

    /* Este method se ejecuta antes de cada test. Aquí
    inicializamos nuestro caso de uso (la lambda), inyectándole los mocks.*/
    @BeforeEach
    void setUp() {
        LoginUserService loginUserService = new LoginUserService();
        this.loginUserUseCase = loginUserService.loginUser(mockUserRepositoryPort, mockTokenServicePort);
    }

    @Test
    void whenValidCredentials_thenShouldReturnUserSessionWithToken() {
        // GIVEN - Preparamos el escenario
        String username = "testuser";
        String password = "password123";
        UserCredentials credentials = new UserCredentials(username, password);
        UserPrincipal userPrincipal = new UserPrincipal(username, password, List.of("ROLE_USER"));
        String expectedToken = "mocked.jwt.token";

        /* Le decimos a nuestro mock del repositorio qué debe hacer
        cuando se le llame con "testuser". Debe devolver un Optional.of(userPrincipal).*/
        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(userPrincipal));

        /* Le decimos a nuestro mock del servicio de tokens qué debe hacer
        cuando se le llame con el UserPrincipal. Debe devolver nuestro token de mock.*/
        when(mockTokenServicePort.apply(userPrincipal)).thenReturn(expectedToken);

        // WHEN - Ejecutamos la acción que queremos probar
        UserSession result = loginUserUseCase.apply(credentials);

        // THEN - Verificamos los resultados
        assertNotNull(result);
        assertEquals(username, result.getUserId());
        assertEquals(expectedToken, result.getJwtToken());

        /* Verificamos que los métodos de nuestros mocks fueron llamados
        exactamente una vez, asegurando que la lógica siguió el camino correcto.*/
        verify(mockUserRepositoryPort, times(1)).apply(username);
        verify(mockTokenServicePort, times(1)).apply(userPrincipal);
    }

    @Test
    void whenInvalidPassword_thenShouldThrowException() {
        // GIVEN
        String username = "testuser";
        String validPassword = "password123";
        String invalidPassword = "wrongpassword";
        UserCredentials credentials = new UserCredentials(username, invalidPassword);
        UserPrincipal userPrincipal = new UserPrincipal(username, validPassword, List.of("ROLE_USER"));

        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.of(userPrincipal));

        // WHEN & THEN - Usamos assertThrows para verificar que se lanza la excepción
        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));

        // Verificamos que el tokenService no fue llamado
        verify(mockTokenServicePort, never()).apply(any());
    }

    @Test
    void whenUserNotFound_thenShouldThrowException() {
        // GIVEN
        String username = "nonexistent";
        UserCredentials credentials = new UserCredentials(username, "anypassword");

        // El mock devuelve un Optional vacío, simulando que el usuario no existe
        when(mockUserRepositoryPort.apply(username)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> loginUserUseCase.apply(credentials));
    }
}