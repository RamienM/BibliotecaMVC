package org.biblioteca.bibliotecamvc.business.exception.log;

public class LogNotFoundException extends RuntimeException {
    public LogNotFoundException(String message) {
        super(message);
    }
}
