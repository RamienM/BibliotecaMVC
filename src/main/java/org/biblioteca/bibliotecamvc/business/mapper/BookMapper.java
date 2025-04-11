package org.biblioteca.bibliotecamvc.business.mapper;

import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.mapper.interfaces.Mapper;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<BookDTO, BookEntity> {

    @Override
    public BookDTO toDTO(BookEntity entity) {
        BookDTO dto = new BookDTO();
        dto.setAuthor(entity.getAuthor());
        dto.setTitle(entity.getTitle());
        dto.setIsbn(entity.getIsbn());
        dto.setBooked(entity.getBooked());
        return dto;
    }

    @Override
    public BookEntity toEntity(BookDTO dto) {
        BookEntity entity = new BookEntity();
        entity.setAuthor(dto.getAuthor());
        entity.setTitle(dto.getTitle());
        entity.setIsbn(dto.getIsbn());
        return entity;
    }
}
