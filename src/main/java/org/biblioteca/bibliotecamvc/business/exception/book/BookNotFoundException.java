package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
