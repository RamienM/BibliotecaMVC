package org.biblioteca.bibliotecamvc.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.BorrowEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.LibraryJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class LibraryRepositoryTest {

    @Autowired
    private LibraryJPARepository libraryRepository;

    @Test
    void saveTest(){
        //Arrange
        LibraryEntity lib = new LibraryEntity();
        lib.setName("Hola");

        //Act
        var resp = libraryRepository.save(lib);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(lib.getName(), resp.getName());
    }

    @Test
    void getByIdTest(){
        //Arrange
        var lib = libraryRepository.save(new LibraryEntity());

        //Act
        var resp = libraryRepository.findById(lib.getId());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(lib.getId(),resp.get().getId());
    }

    @Test
    void getByIdName(){
        //Arrange
        var lib = new LibraryEntity();
        lib.setName("Hola");
        libraryRepository.save(lib);

        //Act
        var resp = libraryRepository.findByName(lib.getName());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(lib.getName(),resp.get().getName());
    }

    @Test
    void updateTest(){
        //Arrange
        var lib = new LibraryEntity();
        lib.setName("Hola");
        var id = libraryRepository.save(lib).getId();

        //Act
        var libraryEntityOptional = libraryRepository.findById(id);
        var libraryEntity = libraryEntityOptional.get();
        libraryEntity.setName("Hola2");
        var resp = libraryRepository.save(libraryEntity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(libraryEntity.getName(), resp.getName());
    }

    @Test
    void deleteTest(){
        //Arrange
        var lib = libraryRepository.save(new LibraryEntity());

        //Act
        libraryRepository.delete(lib);
        var resp = libraryRepository.findById(lib.getId());

        //Assert
        Assertions.assertTrue(resp.isEmpty());
    }

    @Test
    void getAllLibrariesTest(){
        //Arrange
        libraryRepository.save(new LibraryEntity());
        int expectedSize = 1;
        //Act
        var resp = libraryRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllLibrariesEmptyTest(){
        //Arrange
        int expectedSize = 0;
        //Act
        var resp = libraryRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllLibrariesWhenDeletedIsFalseTest(){
        //Arrange
        libraryRepository.save(new LibraryEntity());
        int expectedSize = 1;
        //Act
        var resp = libraryRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }
    @Test
    void getAllLibrariesWhenDeletedIsFalseEmptyTest(){
        //Arrange
        var lib = new LibraryEntity();
        lib.setDeleted(true);
        libraryRepository.save(lib);
        int expectedSize = 0;
        //Act
        var resp = libraryRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

}
