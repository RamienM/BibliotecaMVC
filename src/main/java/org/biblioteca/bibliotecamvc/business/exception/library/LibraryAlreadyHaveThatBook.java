package org.biblioteca.bibliotecamvc.business.exception.library;

public class LibraryAlreadyHaveThatBook extends RuntimeException {
  public LibraryAlreadyHaveThatBook(String message) {
    super(message);
  }
}
