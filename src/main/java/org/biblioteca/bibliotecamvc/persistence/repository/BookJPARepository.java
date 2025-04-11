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

    @Query("select b from BookEntity b join b.users u on u.id =:id where b.booked = true and b.deleted = false ")
    List<BookEntity> getAllByUsers_Id(Integer id);

    @Query("select b from BookEntity b join b.users u on u.id =:id")
    List<BookEntity> getLogs(Integer id);
    
    boolean existsByIsbnAndUsers_Id(String isbn, Integer userId);

    List<BookEntity> getAllByDeleted_False();
}
