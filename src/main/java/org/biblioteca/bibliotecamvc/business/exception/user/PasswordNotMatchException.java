package org.biblioteca.bibliotecamvc.business.exception.user;

public class PasswordNotMatchException extends RuntimeException {
  public PasswordNotMatchException(String message) {
    super(message);
  }
}
