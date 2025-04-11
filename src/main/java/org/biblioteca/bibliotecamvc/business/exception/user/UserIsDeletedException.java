package org.biblioteca.bibliotecamvc.business.exception.user;

public class UserIsDeletedException extends RuntimeException {
    public UserIsDeletedException(String message) {
        super(message);
    }
}
