package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookNotFound extends RuntimeException {
    public BookNotFound(String message) {
        super(message);
    }
}
