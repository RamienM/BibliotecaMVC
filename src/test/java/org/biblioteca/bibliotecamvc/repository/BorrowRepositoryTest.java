package org.biblioteca.bibliotecamvc.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.BorrowEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.BookJPARepository;
import org.biblioteca.bibliotecamvc.persistence.repository.BorrowJPARepository;
import org.biblioteca.bibliotecamvc.persistence.repository.LogJPARepository;
import org.biblioteca.bibliotecamvc.persistence.repository.UserJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class BorrowRepositoryTest {

    @Autowired
    private BorrowJPARepository borrowRepository;
    @Autowired
    private UserJPARepository userRepository;
    @Autowired
    private LogJPARepository logRepository;
    @Autowired
    private BookJPARepository bookRepository;


    @Test
    void saveTest(){
        //Arrange
        var bo = new BorrowEntity();
        bo.setReturned(true);

        //Act
        var resp = borrowRepository.save(bo);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bo.getReturned(), resp.getReturned());
    }

    @Test
    void findByIdTest(){
        //Arrange
        var id = borrowRepository.save(new BorrowEntity()).getId();
        //Act
        var bo = borrowRepository.findById(id).get();
        //Assert
        Assertions.assertNotNull(bo);
        Assertions.assertEquals(id, bo.getId());
    }

    @Test
    void findByUser_IdAndLog_IdTest(){
        //Arrange
        var user = userRepository.save(new UserEntity());
        var log = logRepository.save(new LogEntity());
        var bo = new BorrowEntity();
        bo.setUser(user);
        bo.setLog(log);
        var idBorrow = borrowRepository.save(bo).getId();

        //Act
        var resp = borrowRepository.findByUser_IdAndLog_Id(user.getId(), log.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(idBorrow,resp.get().getId());
    }

    @Test
    void findByUser_idAndLog_BookEntity_Isbn(){
        //Arrange
        var logEntity = new LogEntity();
        var bookEntity = new BookEntity();
        bookEntity.setIsbn("Hola");

        var user = userRepository.save(new UserEntity());
        var book = bookRepository.save(bookEntity);

        logEntity.setBookEntity(book);

        var log = logRepository.save(logEntity);
        var bo = new BorrowEntity();

        bo.setUser(user);
        bo.setLog(log);

        var idBorrow = borrowRepository.save(bo).getId();

        //Act
        var resp = borrowRepository.findByUser_idAndLog_BookEntity_Isbn(user.getId(), book.getIsbn());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(idBorrow,resp.get().getId());
    }

    @Test
    void updateTest(){
        //Arrange
        var id = borrowRepository.save(new BorrowEntity()).getId();
        //Act
        var borrowEntityOptional = borrowRepository.findById(id);
        var bo = borrowEntityOptional.get();
        bo.setReturned(false);
        var resp = borrowRepository.save(bo);
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bo.getReturned(), resp.getReturned());
    }

    @Test
    void deleteTest(){
        //Arrange
        var bo = borrowRepository.save(new BorrowEntity());
        //Act
        borrowRepository.delete(bo);
        var resp = borrowRepository.findById(bo.getId());
        //Assert
        Assertions.assertTrue(resp.isEmpty());
    }

    @Test
    void getAllBorrowsTest(){
        //Arrange
        borrowRepository.save(new BorrowEntity());
        int expectedSize = 1;
        //Act
        var resp = borrowRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllBorrowsEmptyTest(){
        //Arrange
        int expectedSize = 0;
        //Act
        var resp = borrowRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }
}
