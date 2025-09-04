package dev.pedronube.authmanager.infrastructure.exceptionhandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import dev.pedronube.authmanager.domain.exceptions.InvalidCredentialsException;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerUnitTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    public static final String ERROR_MESSAGE = "Las credenciales indicadas no son válidas";

    @BeforeEach
    void setUp() {
        when(messageSource.getMessage(eq(InvalidCredentialsException.ERROR_MESSAGE), any(), any()))
                .thenReturn(ERROR_MESSAGE);
    }

    @Test
    void shouldReturnErrorMessageWhenInvalidCredentialsException() {
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handlerInvalidCredentialsException();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(ERROR_MESSAGE, response.getBody().get("message"));
    }
}





















