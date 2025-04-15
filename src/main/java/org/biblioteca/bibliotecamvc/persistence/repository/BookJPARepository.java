package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookJPARepository extends JpaRepository<BookEntity, Integer> {
    Optional<BookEntity> getByIsbn(String isbn);
    List<BookEntity> getAllByDeleted_False();

    @Query("select b from UserEntity u join u.borrows bo join bo.log lo on lo.deleted = false join lo.bookEntity b on b.deleted=false join lo.libraryEntity li on li.deleted = false where u.id = :id and bo.returned = false")
    List<BookEntity> getAllBooksByUser(Integer id);
}
