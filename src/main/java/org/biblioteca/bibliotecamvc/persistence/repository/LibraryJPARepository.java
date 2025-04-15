package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryJPARepository extends JpaRepository<LibraryEntity, Integer> {
    List<LibraryEntity> getAllByDeleted_False();
    Optional<LibraryEntity> findByName(String name);
}
