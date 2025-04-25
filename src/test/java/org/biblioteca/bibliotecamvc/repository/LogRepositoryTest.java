package org.biblioteca.bibliotecamvc.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.*;
import org.biblioteca.bibliotecamvc.persistence.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class LogRepositoryTest {

    @Autowired
    private LogJPARepository logRepository;
    @Autowired
    private BookJPARepository bookRepository;
    @Autowired
    private LibraryJPARepository libraryRepository;
    @Autowired
    private UserJPARepository userRepository;
    @Autowired
    private BorrowJPARepository borrowRepository;

    @Test
    void saveTest(){
        //Arrange
        var log = new LogEntity();
        log.setBooked(true);

        //Act
        var resp = logRepository.save(log);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(log.getBooked(), resp.getBooked());
    }

    @Test
    void findByIdTest(){
        //Arrange
        var log = logRepository.save(new LogEntity());

        //Act
        var resp = logRepository.findById(log.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(log.getId(),resp.get().getId());
    }

    @Test
    void findByLibraryEntity_IdAndBookEntity_IsbnTest(){
        //Arrange
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn("123");
        bookRepository.save(bookEntity);

        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity = libraryRepository.save(libraryEntity);

        LogEntity logEntity = new LogEntity();
        logEntity.setBookEntity(bookEntity);
        logEntity.setLibraryEntity(libraryEntity);
        logEntity = logRepository.save(logEntity);

        //Act
        var resp = logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(libraryEntity.getId(),bookEntity.getIsbn());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(logEntity.getId(),resp.get().getId());

    }

    @Test
    void updateTest(){
        //Arrange
        var log = logRepository.save(new LogEntity());

        //Act
        var update = logRepository.findById(log.getId());
        var logEntity = update.get();
        logEntity.setBooked(true);
        var resp = logRepository.save(logEntity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(logEntity.getBooked(), resp.getBooked());
    }

    @Test
    void deleteTest(){
        //Arrange
        var log = logRepository.save(new LogEntity());

        //Act
        logRepository.delete(log);
        var resp = logRepository.findById(log.getId());

        //Assert
        Assertions.assertTrue(resp.isEmpty());
    }

    @Test
    void getAllLogsTest(){
        //Arrange
        logRepository.save(new LogEntity());
        int expectedSize = 1;
        //Act
        var resp = logRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }

    @Test
    void getAllLogsEmptyTest(){
        //Arrange
        int expectedSize = 0;
        //Act
        var resp = logRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllLogsByUsers_IdTest(){
        //Arrange
        var user = userRepository.save(new UserEntity());
        var log = logRepository.save(new LogEntity());
        BorrowEntity borrowEntity = new BorrowEntity();
        borrowEntity.setUser(user);
        borrowEntity.setLog(log);
        borrowRepository.save(borrowEntity);

        int expectedSize = 1;

        //Act
        var resp = logRepository.getAllByUsers_Id(user.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }

    @Test
    void getAllLogsByUsers_IdEmptyTest(){
        //Arrange
        var user = userRepository.save(new UserEntity());
        int expectedSize = 0;

        //Act
        var resp = logRepository.getAllByUsers_Id(user.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }

    @Test
    void getAllBooksAvailableByLibrarian_IdTest(){
        //Arrange
        var bookEntity = bookRepository.save(new BookEntity());
        var libraryEntity = libraryRepository.save(new LibraryEntity());
        LogEntity logEntity = new LogEntity();
        logEntity.setBookEntity(bookEntity);
        logEntity.setLibraryEntity(libraryEntity);
        logRepository.save(logEntity);

        int expectedSize = 1;
        //Act

        var resp = logRepository.getAllBooksAvailableByLibrarian_Id(libraryEntity.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }

    @Test
    void getAllBooksAvailableByLibrarian_IdEmptyTest(){
        //Arrange
        var bookEntity = bookRepository.save(new BookEntity());
        var libraryEntity = libraryRepository.save(new LibraryEntity());
        LogEntity logEntity = new LogEntity();
        logEntity.setBookEntity(bookEntity);
        logEntity.setLibraryEntity(libraryEntity);
        logEntity.setBooked(true);
        logRepository.save(logEntity);

        int expectedSize = 0;
        //Act

        var resp = logRepository.getAllBooksAvailableByLibrarian_Id(libraryEntity.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }
}
