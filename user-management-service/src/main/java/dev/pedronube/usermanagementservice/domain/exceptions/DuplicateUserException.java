package dev.pedronube.usermanagementservice.domain.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("The username is already in use");
    }
}
