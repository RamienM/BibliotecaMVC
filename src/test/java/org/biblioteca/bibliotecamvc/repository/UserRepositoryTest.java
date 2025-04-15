package org.biblioteca.bibliotecamvc.repository;

import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
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
public class UserRepositoryTest {

    @Autowired
    private UserJPARepository userRepository;


    @Test
    void saveTest(){
        //Arrange
        var user = new UserEntity();
        user.setUsername("test");

        //Act
        var resp = userRepository.save(user);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(user.getUsername(), resp.getUsername());
    }

    @Test
    void findByIdTest(){
        //Arrange
        var id = userRepository.save(new UserEntity()).getId();

        //Act

        var resp = userRepository.findById(id);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(id,resp.get().getId());
    }

    @Test
    void findByIdUsernameTest(){
        //Arrange
        var user =new UserEntity();
        user.setUsername("test");
        userRepository.save(user);

        //Act
        var resp = userRepository.findByUsername(user.getUsername());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(user.getUsername(),resp.get().getUsername());
    }

    @Test
    void updateTest(){
        //Arrange
        var user = new UserEntity();
        user.setUsername("test");
        var userSaved = userRepository.save(user);

        //Act
        var userEntityOptional = userRepository.findById(userSaved.getId());
        var userEntity = userEntityOptional.get();
        userEntity.setUsername("update");
        var resp = userRepository.save(userEntity);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(userEntity.getUsername(), resp.getUsername());
    }

    @Test
    void deleteTest(){
        //Arrange
        var user = userRepository.save(new UserEntity());

        //Act
        userRepository.delete(user);
        var resp = userRepository.findById(user.getId());
        //Assert
        Assertions.assertTrue(resp.isEmpty());
    }

    @Test
    void getAllUsersTest(){
        //Arrange
        userRepository.save(new UserEntity());
        int expectedSize = 1;
        //Act
        var resp = userRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllUsersEmptyTest(){
        //Arrange
        int expectedSize = 0;
        //Act
        var resp = userRepository.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllLibrariesWhenDeletedIsFalseTest(){
        //Arrange
        userRepository.save(new UserEntity());
        int expectedSize = 1;
        //Act
        var resp = userRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

    @Test
    void getAllLibrariesWhenDeletedIsFalseEmptyTest(){
        //Arrange
        var user = new UserEntity();
        user.setDeleted(true);
        userRepository.save(user);
        int expectedSize = 0;
        //Act
        var resp = userRepository.getAllByDeleted_False();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());

    }

}
