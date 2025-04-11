package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "tittle")
    private String title;
    @Column(name = "author")
    private String author;
}
