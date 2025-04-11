package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookIsBookedException extends RuntimeException {
    public BookIsBookedException(String message) {
        super(message);
    }
}
