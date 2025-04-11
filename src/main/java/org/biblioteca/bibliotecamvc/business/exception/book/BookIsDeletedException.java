package org.biblioteca.bibliotecamvc.business.exception.book;

public class BookIsDeletedException extends RuntimeException {
    public BookIsDeletedException(String message) {
        super(message);
    }
}
