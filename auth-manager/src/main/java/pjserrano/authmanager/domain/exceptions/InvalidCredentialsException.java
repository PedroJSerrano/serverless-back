package pjserrano.authmanager.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public static final String ERROR_MESSAGE = "Las credenciales indicadas no son válidas";

    public InvalidCredentialsException() {
        super(ERROR_MESSAGE);
    }
    public InvalidCredentialsException(String message) {
        super(message);
    }
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }
}
