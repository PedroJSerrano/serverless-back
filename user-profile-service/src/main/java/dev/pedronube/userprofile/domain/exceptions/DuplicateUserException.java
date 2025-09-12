package dev.pedronube.userprofile.domain.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("The username is already in use");
    }
}
