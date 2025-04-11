package org.biblioteca.bibliotecamvc.business.mapper.interfaces;


import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.persistence.entities.UserEntity;

public class UserMapper implements Mapper<UserDTO, UserEntity> {
    @Override
    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setName(entity.getUsername());
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        return null;
    }
}
