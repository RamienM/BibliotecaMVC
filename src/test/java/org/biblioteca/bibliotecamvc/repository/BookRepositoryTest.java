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
public class BookRepositoryTest {
    @Autowired
    private BookJPARepository bookRepository;
    @Autowired
    private UserJPARepository userRepository;
    @Autowired
    private BorrowJPARepository borrowRepository;
    @Autowired
    private LogJPARepository logRepository;
    @Autowired
    private LibraryJPARepository libraryRepository;

    @Test
    void saveTest(){
        //Arrange
        var book = new BookEntity();
        book.setTitle("Book Title");
        //Act
        var resp = bookRepository.save(book);
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(book.getTitle(), resp.getTitle());
    }

    @Test
    void findByIdTest(){
        //Arrange
        var book = bookRepository.save(new BookEntity());

        //Act
        var resp = bookRepository.findById(book.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(book.getId(),resp.get().getId());
    }

    @Test
    void findByIsbnTest(){
        //Arrange
        var book = new BookEntity();
        book.setIsbn("1");
        var bookSaved = bookRepository.save(book);

        //Act
        var resp = bookRepository.getByIsbn(book.getIsbn());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bookSaved.getIsbn(), resp.get().getIsbn());
    }


    @Test
    void updateTest(){
        //Arrange
        var book = new BookEntity();
        book.setTitle("Book Title");
        var savedBook = bookRepository.save(book);

        //Act
        var bookEntityOptional = bookRepository.findById(savedBook.getId());
        var bookEntity = bookEntityOptional.get();
        bookEntity.setTitle("New Title");
        var resp = bookRepository.save(bookEntity);
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bookEntity.getTitle(),resp.getTitle());
    }

    @Test
    void deleteTest(){
        //Arrange
        var book = bookRepository.save(new BookEntity());

        //Act
        bookRepository.delete(book);
        var resp = bookRepository.findById(book.getId());
        //Assert
        Assertions.assertTrue(resp.isEmpty());
    }

    @Test
    void getAllBooksTest(){
        //Arrange
        bookRepository.save(new BookEntity());
        int expectedSize = 1;
        //Act
        var resp = bookRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }
    @Test
    void getAllBooksEmptyTest(){
        //Arrange
        int expectedSize = 0;
        //Act
        var resp = bookRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllBooksWhenDeletedIsFalseTest(){
        //Arrange
        bookRepository.save(new BookEntity());
        int expectedSize = 1;
        //Act
        var resp = bookRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }
    @Test
    void getAllBooksWhenDeletedIsFalseEmptyTest(){
        //Arrange
        var book = new BookEntity();
        book.setDeleted(true);
        bookRepository.save(book);
        int expectedSize = 0;
        //Act
        var resp = bookRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    //TODO
    @Test
    void getAllBooksByUserIdTest(){
        //Arrange
        var user = userRepository.save(new UserEntity());
        var book = bookRepository.save(new BookEntity());
        var library = libraryRepository.save(new LibraryEntity());

        var logEntity = new LogEntity();
        logEntity.setBookEntity(book);
        logEntity.setLibraryEntity(library);
        var log = logRepository.save(logEntity);

        var borrowEntity = new BorrowEntity();
        borrowEntity.setUser(user);
        borrowEntity.setLog(log);

        borrowRepository.save(borrowEntity);

        int expectedSize = 1;

        //Act
        var resp = bookRepository.getAllBooksByUser(user.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
    }

}
