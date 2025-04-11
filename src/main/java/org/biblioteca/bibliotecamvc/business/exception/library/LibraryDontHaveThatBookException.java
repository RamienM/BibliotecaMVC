package org.biblioteca.bibliotecamvc.business.exception.library;

public class LibraryDontHaveThatBookException extends RuntimeException
{
    public LibraryDontHaveThatBookException(String message) {
        super(message);
    }
}
