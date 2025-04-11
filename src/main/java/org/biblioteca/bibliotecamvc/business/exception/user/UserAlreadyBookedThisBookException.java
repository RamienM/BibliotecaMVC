package org.biblioteca.bibliotecamvc.business.exception.user;

public class UserAlreadyBookedThisBookException extends RuntimeException {
    public UserAlreadyBookedThisBookException(String message) {
        super(message);
    }
}
