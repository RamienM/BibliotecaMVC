package org.biblioteca.bibliotecamvc.business.mapper;

import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.mapper.interfaces.Mapper;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper implements Mapper<LibraryDTO, LibraryEntity> {
    @Override
    public LibraryDTO toDTO(LibraryEntity entity) {
        LibraryDTO dto = new LibraryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public LibraryEntity toEntity(LibraryDTO dto) {
        LibraryEntity entity = new LibraryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
