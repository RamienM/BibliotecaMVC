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

    @Column(name = "booked")
    private Boolean booked = false; //Lo ideal es un enumerado, va a desaparecer

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToMany
    @JoinTable(
            name = "book_user",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
            )
    private List<UserEntity> users = new ArrayList<>();


    @OneToMany(mappedBy = "bookEntity")
    private List<LogEntity> logs = new ArrayList<>();
}
