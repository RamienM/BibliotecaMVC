package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Library")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
}
