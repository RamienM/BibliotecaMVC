package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "borrow")
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "returned")
    private Boolean returned = false;

    @ManyToOne
    private LogEntity log;

    @ManyToOne
    private UserEntity user;
}
