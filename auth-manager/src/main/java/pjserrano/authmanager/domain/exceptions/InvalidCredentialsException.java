package pjserrano.authmanager.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public static final String ERROR_MESSAGE = "messages.error.auth.invalid-credentials";
    public InvalidCredentialsException() {
        super(ERROR_MESSAGE);
    }
}
