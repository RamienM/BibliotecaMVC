package org.biblioteca.bibliotecamvc.business.exception.borrow;

public class BorrowNotFoundException extends RuntimeException {
    public BorrowNotFoundException(String message) {
        super(message);
    }
}
