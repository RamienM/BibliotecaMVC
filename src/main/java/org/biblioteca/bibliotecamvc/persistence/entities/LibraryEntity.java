package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Library")
public class LibraryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToMany
    @JoinTable(
            name = "library_book",
            joinColumns = @JoinColumn(name = "library_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<BookEntity> bookEntities = new ArrayList<>(); //Crea una tabla intermedia

    @OneToMany(mappedBy = "libraryEntity")
    private List<LogEntity> logs = new ArrayList<>();
}
