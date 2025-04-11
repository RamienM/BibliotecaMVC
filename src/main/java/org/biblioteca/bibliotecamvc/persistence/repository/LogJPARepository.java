package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LogJPARepository extends JpaRepository<LogEntity, Integer> {

    List<LogEntity> getAllByUsers_Id(Integer id);

    @Query("select l from LogEntity l join l.libraryEntity li on li.id =:id where l.booked = false ")
    List<LogEntity> getAllBooksAvailableByLibrarian_Id(Integer id);

    Optional<LogEntity> findByLibraryEntity_IdAndBookEntity_Id(Integer libraryEntityId, Integer bookEntityId);
}
