package org.biblioteca.bibliotecamvc.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private String title;
    private String author;
    private String isbn;
    private Boolean booked;
}
