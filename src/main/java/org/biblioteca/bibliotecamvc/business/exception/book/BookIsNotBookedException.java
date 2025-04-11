package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookIsNotBookedException extends RuntimeException {
    public BookIsNotBookedException(String message) {
        super(message);
    }
}
