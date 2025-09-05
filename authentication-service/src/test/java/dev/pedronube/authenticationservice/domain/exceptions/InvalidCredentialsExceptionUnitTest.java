package dev.pedronube.authenticationservice.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InvalidCredentialsExceptionUnitTest {

    @Test
    void testNoArgsConstructor() {
        InvalidCredentialsException exception = new InvalidCredentialsException();
        assertNotNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testWithMessageConstructor() {
        String customMessage = "Credenciales inválidas, intenta de nuevo.";
        InvalidCredentialsException exception = new InvalidCredentialsException(customMessage);
        assertEquals(customMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testWithMessageAndCauseConstructor() {
        String customMessage = "Error de autenticación: usuario o contraseña incorrectos.";
        Throwable cause = new IllegalArgumentException("Usuario no encontrado.");
        InvalidCredentialsException exception = new InvalidCredentialsException(customMessage, cause);
        assertEquals(customMessage, exception.getMessage());
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertEquals("Usuario no encontrado.", exception.getCause().getMessage());
    }

    @Test
    void testWithCauseConstructor() {
        Throwable cause = new IllegalStateException("Conexión a la base de datos fallida.");
        InvalidCredentialsException exception = new InvalidCredentialsException(cause);
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }
}