package org.biblioteca.bibliotecamvc.business.mapper;


import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.mapper.interfaces.Mapper;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserDTO, UserEntity> {
    @Override
    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setAdmin(entity.getAdmin());
        return dto;
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        UserEntity dtoEntity = new UserEntity();
        dtoEntity.setUsername(dto.getUsername());
        dtoEntity.setAdmin(dto.getAdmin());
        return dtoEntity;
    }

    public UserEntity toEntityByRegister(UserRegisterDTO dto) {
        UserEntity dtoEntity = new UserEntity();
        dtoEntity.setUsername(dto.getUsername());
        dtoEntity.setPassword(dto.getPassword());
        dtoEntity.setAdmin(dto.getAdmin());
        return dtoEntity;
    }
}
