package org.biblioteca.bibliotecamvc.mapper;

import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.mapper.UserMapper;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void entityToDTOTest(){
        //Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Test User");
        userEntity.setId(1);
        userEntity.setAdmin(true);
        //Act
        var resp = userMapper.toDTO(userEntity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(userEntity.getUsername(), resp.getUsername());
        Assertions.assertEquals(userEntity.getId(), resp.getId());
        Assertions.assertEquals(userEntity.getAdmin(), resp.getAdmin());
    }

    @Test
    void dTOtoEntityTest(){
        //Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Test User");
        userDTO.setAdmin(true);

        //Act
        var resp = userMapper.toEntity(userDTO);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(userDTO.getUsername(), resp.getUsername());
        Assertions.assertEquals(userDTO.getAdmin(), resp.getAdmin());
    }

    @Test
    void dtoRegistertoEntityTest(){
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername("Test User");
        userDTO.setPassword("Test Password");
        userDTO.setAdmin(false);

        // Act
        var resp = userMapper.toEntityByRegister(userDTO);

        // Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(userDTO.getUsername(), resp.getUsername());
        Assertions.assertEquals(userDTO.getPassword(), resp.getPassword());
        Assertions.assertEquals(userDTO.getAdmin(), resp.getAdmin());
    }
}
