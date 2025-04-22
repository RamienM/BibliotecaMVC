package org.biblioteca.bibliotecamvc.business;

import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LogDTO;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.mapper.LogMapper;
import org.biblioteca.bibliotecamvc.business.service.LogService;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LogEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.LogJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
    @Mock
    private LogJPARepository logRepository;

    @Mock
    private LogMapper logMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private LogService logService;

    @Test
    void getAllLogsByUserIdTest(){
        //Arrange

        var logs = new ArrayList<LogEntity>();
        logs.add(new LogEntity());

        var logsDTO = new ArrayList<LogDTO>();
        logsDTO.add(new LogDTO());

        int userId = 1;
        int expectedSize = 1;

        when(logRepository.getAllByUsers_Id(any(Integer.class))).thenReturn(logs);
        when(logMapper.toDTO(any(LogEntity.class))).thenReturn(logsDTO.get(0));

        //Act
        var resp = logService.getAllLogsByUserId(userId);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(logRepository, times(1)).getAllByUsers_Id(any(Integer.class));
        verify(logMapper, times(1)).toDTO(any(LogEntity.class));
    }

    @Test
    void getAllLogsByUserIdEmptyTest(){
        //Arrange
        int userId = 1;
        int expectedSize = 0;

        when(logRepository.getAllByUsers_Id(any(Integer.class))).thenReturn(new ArrayList<>());

        //Act
        var resp = logService.getAllLogsByUserId(userId);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(logRepository, times(1)).getAllByUsers_Id(any(Integer.class));
    }

    @Test
    void getAllBooksAvailableByLibraryIdTest(){
        //Arrange
        int libraryId = 1;
        int expectedSize = 1;

        ArrayList<BookDTO> bookDTOS = new ArrayList<>();
        bookDTOS.add(new BookDTO());

        ArrayList<LogEntity> logs = new ArrayList<>();
        LogEntity logEntity = new LogEntity();
        logEntity.setBookEntity(new BookEntity());
        logs.add(logEntity);

        when(logRepository.getAllBooksAvailableByLibrarian_Id(any(Integer.class))).thenReturn(logs);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(bookDTOS.get(0));

        //Act
        var resp = logService.getAllBooksAvailableByLibraryId(libraryId);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(logRepository, times(1)).getAllBooksAvailableByLibrarian_Id(any(Integer.class));
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
    }

    @Test
    void getAllBooksAvailableByLibraryIdEmptyTest(){
        //Arrange
        int libraryId = 1;
        int expectedSize = 0;

        when(logRepository.getAllBooksAvailableByLibrarian_Id(any(Integer.class))).thenReturn(new ArrayList<>());

        //Act
        var resp = logService.getAllBooksAvailableByLibraryId(libraryId);

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(logRepository, times(1)).getAllBooksAvailableByLibrarian_Id(any(Integer.class));
    }
}
