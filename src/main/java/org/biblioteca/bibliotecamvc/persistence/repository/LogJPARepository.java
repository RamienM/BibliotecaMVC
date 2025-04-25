package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LogJPARepository extends JpaRepository<LogEntity, Integer> {

    @Query("select lo from UserEntity u join u.borrows bo join bo.log lo where u.id = :id")
    List<LogEntity> getAllByUsers_Id(Integer id);

    @Query("select l from LogEntity l join l.libraryEntity li on li.id =:id join l.bookEntity bo on bo.deleted = false where l.booked = false and l.deleted=false ")
    List<LogEntity> getAllBooksAvailableByLibrarian_Id(Integer id);

    Optional<LogEntity> findByLibraryEntity_IdAndBookEntity_Isbn(Integer libraryEntityId, String bookEntityId);
}
