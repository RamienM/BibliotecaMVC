package org.biblioteca.bibliotecamvc.business.exception.library;

public class LibraryNotFoundException extends RuntimeException {
    public LibraryNotFoundException(String message) {
        super(message);
    }
}
