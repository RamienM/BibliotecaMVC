package org.biblioteca.bibliotecamvc.business.mapper;

import org.biblioteca.bibliotecamvc.business.dto.LogDTO;
import org.biblioteca.bibliotecamvc.business.mapper.interfaces.Mapper;
import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.springframework.stereotype.Component;

@Component
public class LogMapper implements Mapper<LogDTO, LogEntity> {
    @Override
    public LogDTO toDTO(LogEntity entity) {
        LogDTO dto = new LogDTO();
        dto.setBookAuthor(entity.getBookEntity().getAuthor());
        dto.setBookTitle(entity.getBookEntity().getTitle());
        dto.setLibraryName(entity.getLibraryEntity().getName());
        return dto;
    }

    //Sin implementacion
    @Override
    public LogEntity toEntity(LogDTO dto) {
        return null;
    }
}
