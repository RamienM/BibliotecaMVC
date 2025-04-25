package org.biblioteca.bibliotecamvc.mapper;

import org.biblioteca.bibliotecamvc.business.mapper.LogMapper;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogMapperTest {
    @Autowired
    private LogMapper logMapper;

    @Test
    void entityToDTOTest(){
        //Arrange
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setName("Test Library");
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle("Test Book");
        bookEntity.setAuthor("Test Author");
        LogEntity entity = new LogEntity();
        entity.setBookEntity(bookEntity);
        entity.setLibraryEntity(libraryEntity);

        //Act
        var resp = logMapper.toDTO(entity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bookEntity.getAuthor(), resp.getBookAuthor());
        Assertions.assertEquals(bookEntity.getTitle(),resp.getBookTitle());
        Assertions.assertEquals(libraryEntity.getName(), resp.getLibraryName());

    }

}
