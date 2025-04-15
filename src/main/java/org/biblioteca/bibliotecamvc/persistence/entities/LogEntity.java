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
    private Boolean booked = false;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne
    private BookEntity bookEntity;

    @ManyToOne
    private LibraryEntity libraryEntity;

    @OneToMany(mappedBy = "log")
    private List<BorrowEntity> borrows = new ArrayList<>();
}
