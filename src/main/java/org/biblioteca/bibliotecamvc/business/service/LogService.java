package org.biblioteca.bibliotecamvc.business.service;

import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LogDTO;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.mapper.LogMapper;
import org.biblioteca.bibliotecamvc.persistence.repository.LogJPARepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LogService {
    private final LogMapper logMapper;
    private final BookMapper bookMapper;
    private final LogJPARepository repository;

    /**
     * Obtención del registro de libros que han estado en posesion de un usuario.
     * @param userId    ID del Usuario
     * @return  Registro de libros
     */
    public List<LogDTO> getAllLogsByUserId(Integer userId) {
        List<LogDTO> logDTOS = new ArrayList<>();
        repository.getAllByUsers_Id(userId).forEach(log -> logDTOS.add( logMapper.toDTO(log)));
        return logDTOS;
    }

    /**
     * Obtención de todos los libres disponibles de una biblioteca.
     * @param libraryId ID de la biblioteca
     * @return Lista de libros de disponibles
     */
    public List<BookDTO> getAllBooksAvailableByLibraryId(Integer libraryId) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        repository.getAllBooksAvailableByLibrarian_Id(libraryId).forEach(log -> bookDTOS.add(bookMapper.toDTO(log.getBookEntity())));
        return  bookDTOS;
    }

}
