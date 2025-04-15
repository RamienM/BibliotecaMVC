package org.biblioteca.bibliotecamvc.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "User_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name= "admin")
    private Boolean admin = false; //Lo ideal es un enumerado

     @Column(name = "deleted")
     private Boolean deleted = false;

    @OneToMany(mappedBy = "user")
    private List<BorrowEntity> borrows = new ArrayList<>();
}
