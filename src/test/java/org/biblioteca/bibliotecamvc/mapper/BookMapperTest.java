package org.biblioteca.bibliotecamvc.mapper;

import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void entityToDTOTest(){
        //Arrange
        BookEntity entity = new BookEntity();
        entity.setIsbn("123");
        entity.setTitle("Title");
        entity.setAuthor("Author");

        //Act
        var dto = bookMapper.toDTO(entity);

        //Assert
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(entity.getIsbn(), dto.getIsbn());
        Assertions.assertEquals(entity.getTitle(), dto.getTitle());
        Assertions.assertEquals(entity.getAuthor(), dto.getAuthor());
    }

    @Test
    void dTOtoEntityTest(){
        //Arrange
        BookDTO dto = new BookDTO();
        dto.setIsbn("123");
        dto.setTitle("Title");
        dto.setAuthor("Author");

        //Act
        var entity = bookMapper.toEntity(dto);

        //Assert
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(dto.getIsbn(), entity.getIsbn());
        Assertions.assertEquals(dto.getTitle(), entity.getTitle());
        Assertions.assertEquals(dto.getAuthor(), entity.getAuthor());
    }
}
