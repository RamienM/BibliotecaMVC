package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Book")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "tittle")
    private String title;
    @Column(name = "author")
    private String author;

    @Column(name = "deleted")
    private Boolean deleted = false;


    @OneToMany(mappedBy = "bookEntity")
    private List<LogEntity> logs = new ArrayList<>();
}
