package org.biblioteca.bibliotecamvc.business;

import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.user.PasswordNotMatchException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserIsDeletedException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
import org.biblioteca.bibliotecamvc.business.mapper.UserMapper;
import org.biblioteca.bibliotecamvc.business.service.UserService;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.UserJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserJPARepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUserTest() {
        //Arrange
        UserEntity userEntity = new UserEntity();
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toEntityByRegister(any(UserRegisterDTO.class))).thenReturn(userEntity);

        //Act

        userService.register(userRegisterDTO);
        //Assert
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toEntityByRegister(any(UserRegisterDTO.class));
    }

    @Test
    void saveUserWhenUserAlreadyExistsTest() {
        //Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        //Act

        var resp = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {userService.register(userRegisterDTO);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void fingByIdTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new UserEntity()));
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        //Act
        var resp = userService.findById(id);

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userMapper, times(1)).toDTO(any(UserEntity.class));
    }

    @Test
    void fingByIdWhenUserNotFoundTest() {
        //Arrange
        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.findById(id);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void loginTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        //Act
        var resp = userService.login(userRegisterDTO);

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userMapper, times(1)).toDTO(any(UserEntity.class));
    }

    @Test
    void loginWhenUserNotFoundTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.login(userRegisterDTO);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void loginWhenUserIsDeletedTest() {
        //Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setDeleted(true);

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userEntity));

        //Act
        var resp = Assertions.assertThrows(UserIsDeletedException.class, () -> {userService.login(userRegisterDTO);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void loginWhenPasswordDontMatchTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("hola");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userEntity));

        //Act
        var resp = Assertions.assertThrows(PasswordNotMatchException.class, () -> {userService.login(userRegisterDTO);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void updateUserTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(userDTO);

        //Act
        var resp = userService.update(userDTO,id);

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userMapper, times(1)).toDTO(any(UserEntity.class));

    }

    @Test
    void updateUserWhenUserNotFoundTest() {
        //Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.update(userDTO,id);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void updateRegisterUserTest(){
        //Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        int id = 1;

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        //Act
        userService.update(userRegisterDTO,id);

        //Assert
        verify(userRepository, times(1)).findById(any(Integer.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateRegisterUserWhenUserNotFoundTest(){
        //Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        int id = 1;

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.update(userRegisterDTO,id);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void deleteUserTest() {
        //Arrange
        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new UserEntity()));

        //Act
        userService.delete(id);

        //Assert
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void deleteUserWhenUserNotFoundTest(){
        //Arrange
        String id = "1";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> {userService.delete(id);});

        //Assert
        Assertions.assertNotNull(resp);
        verify(userRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    void findAllUsersTest() {
        //Arrange
        ArrayList<UserEntity> list = new ArrayList<>();
        list.add(new UserEntity());

        ArrayList<UserDTO> listDTO = new ArrayList<>();
        listDTO.add(new UserDTO());

        when(userRepository.getAllByDeleted_False()).thenReturn(list);
        when(userMapper.toDTO(any(UserEntity.class))).thenReturn(listDTO.get(0));

        int expectedSize = 1;

        //Act
        var resp = userService.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(userRepository, times(1)).getAllByDeleted_False();
        verify(userMapper,times(1)).toDTO(any(UserEntity.class));
    }

    @Test
    void findAllUsersEmptyTest() {
        //Arrange
        when(userRepository.getAllByDeleted_False()).thenReturn(new ArrayList<>());

        int expectedSize = 0;

        //Act
        var resp = userService.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(userRepository, times(1)).getAllByDeleted_False();
    }

}
