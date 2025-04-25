package org.biblioteca.bibliotecamvc.persistence.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowJPARepository extends JpaRepository<BorrowEntity, Integer> {

    @Query("select bo from BorrowEntity bo join bo.log l on l.id = :logId join bo.user u on u.id = :userId")
    Optional<BorrowEntity> findByUser_IdAndLog_Id(Integer userId, Integer logId);

    Optional<BorrowEntity> findByUser_idAndLog_BookEntity_Isbn(Integer userId,String isbn);
}

