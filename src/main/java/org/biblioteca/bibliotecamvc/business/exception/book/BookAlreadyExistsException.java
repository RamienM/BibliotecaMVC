package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
