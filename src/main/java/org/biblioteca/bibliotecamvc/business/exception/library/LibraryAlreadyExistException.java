package org.biblioteca.bibliotecamvc.business.exception.library;

public class LibraryAlreadyExistException extends RuntimeException {
    public LibraryAlreadyExistException(String message) {
        super(message);
    }
}
