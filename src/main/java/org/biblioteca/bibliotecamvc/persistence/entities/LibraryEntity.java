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

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @OneToMany(mappedBy = "libraryEntity")
    private List<LogEntity> logs = new ArrayList<>();
}
