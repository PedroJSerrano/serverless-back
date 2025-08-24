package pjserrano.authmanager.infrastructure.exceptionhandler;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pjserrano.authmanager.domain.exceptions.InvalidCredentialsException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handlerInvalidCredentialsException() {
        String errorMessage = messageSource.getMessage
                (InvalidCredentialsException.ERROR_MESSAGE, null, LocaleContextHolder.getLocale());
        Map<String, String> body = Map.of("message", errorMessage);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
























