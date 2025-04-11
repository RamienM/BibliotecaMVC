package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryJPARepository extends JpaRepository<LibraryEntity, Integer> {
    @Query("select b from LibraryEntity l join l.bookEntities b on b.booked = false and b.deleted=false where l.id = :id ")
    List<BookEntity> getAllBooksAvailableById(Integer id);

    List<LibraryEntity> getAllByDeleted_False();
}
