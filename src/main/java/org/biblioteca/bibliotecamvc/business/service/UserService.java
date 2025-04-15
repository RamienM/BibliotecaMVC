package org.biblioteca.bibliotecamvc.business.service;

import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.user.*;
import org.biblioteca.bibliotecamvc.business.mapper.UserMapper;
import org.biblioteca.bibliotecamvc.business.service.interfaces.BasicCRUD;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.UserJPARepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements BasicCRUD<UserDTO,String> {
    private final UserJPARepository userRepository;
    private final UserMapper userMapper;
    private final UserJPARepository userJPARepository;

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> userDTOs = new ArrayList<>();
        userRepository.getAllByDeleted_False().forEach(user -> userDTOs.add(userMapper.toDTO(user)));
        return userDTOs;
    }

    @Override
    public UserDTO findById(String id) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(id);
        if(userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        return userMapper.toDTO(userEntity.get());
    }


    //No se usa, ya que se genera con userDTO y nos interesa UserRegisterDTO
    //Quizás no sea necesario tener 2 DTOs
    @Override
    public UserDTO save(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO userDTO, String id) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(id);
        if(userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        UserEntity oldUser = userEntity.get();
        oldUser.setUsername(userDTO.getUsername().isEmpty() ? oldUser.getUsername() : userDTO.getUsername());
        return userMapper.toDTO(userRepository.save(oldUser));
    }

    /**
     * Actualiza un usuario haciendo uso del DTO userRegisterDTO, esto permite cambio de contraseña.
     * @param userRegisterDTO DTO específica para el registro del usuario.
     * @param id Identidicador del usuario.
     */
    public void update(UserRegisterDTO userRegisterDTO, Integer id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        UserEntity oldUser = userEntity.get();
        oldUser.setUsername(userRegisterDTO.getUsername().isEmpty() ? oldUser.getUsername() : userRegisterDTO.getUsername());
        oldUser.setPassword(userRegisterDTO.getPassword().isEmpty() ? oldUser.getPassword() : userRegisterDTO.getPassword());
        oldUser.setAdmin(userRegisterDTO.getAdmin()==null ? oldUser.getAdmin() : userRegisterDTO.getAdmin());
        userRepository.save(oldUser);
    }

    @Override
    public void delete(String id) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(id);
        if(userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        UserEntity oldUser = userEntity.get();
        oldUser.setDeleted(true);
        userRepository.save(oldUser);
    }

    /**
     * Registra un nuevo usuario a la base de datos
     * @param userRegisterDTO DTO específica para el registro del usuario
     */
    public void register(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(userRegisterDTO.getUsername());
        if(userEntity.isPresent()) throw new UserAlreadyExistsException("User already exists");
        userJPARepository.save(userMapper.toEntityByRegister(userRegisterDTO));
    }

    /**
     * Inicio de sesión de un usuario, devuelve un UserDTO para obtener información como el id y username,
     * viene a intentar hacer los mismo que jwt.
     * @param userRegisterDTO DTO específica para el registro y inicio de sesion.
     * @return Devuelve una DTO con el id y el nombre de usuario
     */
    public UserDTO login(UserRegisterDTO userRegisterDTO) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(userRegisterDTO.getUsername());
        if(userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        UserEntity oldUser = userEntity.get();
        if(oldUser.getDeleted()) throw new UserIsDeletedException("User is deleted");
        if (!oldUser.getPassword().equals(userRegisterDTO.getPassword())) throw new PasswordNotMatchException("Password not match");
        return userMapper.toDTO(oldUser);
    }

}
