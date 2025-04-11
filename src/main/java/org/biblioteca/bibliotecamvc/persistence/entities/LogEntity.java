package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Log")
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "booked")
    private boolean booked = false;

    @ManyToOne
    private BookEntity bookEntity;

    @ManyToOne
    private LibraryEntity libraryEntity;

    @ManyToMany
    @JoinTable(
            name = "log_user",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users = new ArrayList<>();
}
