package org.biblioteca.bibliotecamvc.business;

import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.BookJPARepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookJPARepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBookTest(){
        //Arrange
        var bookDTO = new BookDTO();
        var bookEntity = new BookEntity();

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());
        when(bookMapper.toEntity(bookDTO)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(bookMapper.toDTO(bookEntity)).thenReturn(bookDTO);

        //Act
        var resp = bookService.save(bookDTO);

        //Assert
        assertThat(resp).isNotNull();
        assertThat(resp).isEqualTo(bookDTO);
        verify(bookRepository).getByIsbn(any(String.class));
        verify(bookRepository).save(any(BookEntity.class));
        verify(bookMapper).toDTO(any(BookEntity.class));
        verify(bookMapper).toEntity(any(BookDTO.class));
    }
}
