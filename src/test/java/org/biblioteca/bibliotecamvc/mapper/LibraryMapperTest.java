package org.biblioteca.bibliotecamvc.mapper;

import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.mapper.LibraryMapper;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LibraryMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Test
    void entityToDTOTest(){
        //Arrange
        LibraryEntity entity = new LibraryEntity();
        entity.setName("Test Library");

        //Act
        var resp = libraryMapper.toDTO(entity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(entity.getName(), resp.getName());
    }

    @Test
    void dTOtoEntityTest(){
        //Arrange
        LibraryDTO dto = new LibraryDTO();
        dto.setName("Test Library");

        //Act
        var resp = libraryMapper.toEntity(dto);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(dto.getName(), resp.getName());
    }
}
