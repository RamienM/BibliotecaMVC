package org.biblioteca.bibliotecamvc.business;

import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.BookJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookJPARepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookService bookService;

    @Test
    void saveBookTest(){
        //Arrange
        var bookDTO = new BookDTO();
        bookDTO.setIsbn("123");
        var bookEntity = new BookEntity();

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());
        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(bookEntity);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(bookDTO);

        //Act
        var resp = bookService.save(bookDTO);

        //Assert
        assertThat(resp).isNotNull();
        assertThat(resp).isEqualTo(bookDTO);
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
        verify(bookRepository, times(1)).save(any(BookEntity.class));
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
        verify(bookMapper, times(1)).toEntity(any(BookDTO.class));
    }

    @Test
    void saveBookWhenBookAlreadyExistsTest(){
        //Arrange
        var bookDTO = new BookDTO();
        bookDTO.setIsbn("123");

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        //Act
        var resp = Assertions.assertThrows(BookAlreadyExistsException.class, () -> bookService.save(bookDTO));
        //Assert
        Assertions.assertNotNull(resp);
        verify(bookRepository, times(1)).getByIsbn(any(String.class));

    }

    @Test
    void findByIdTest(){
        //Arrange
        var bookDTO = new BookDTO();
        bookDTO.setIsbn("123");

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(bookDTO);

        //Act
        var resp = bookService.findById(bookDTO.getIsbn());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bookDTO.getIsbn(), resp.getIsbn());
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
    }

    @Test
    void findByIdWhenBookNotFoundTest(){
        //Arrange
        var bookDTO = new BookDTO();
        bookDTO.setIsbn("123");

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());
        //Act

        var resp = Assertions.assertThrows(BookNotFoundException.class, () -> bookService.findById(bookDTO.getIsbn()));

        //Assert
        Assertions.assertNotNull(resp);
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
    }

    @Test
    void updateBookTest(){
        //Arrange
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("123");

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(bookDTO);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(new BookEntity());

        //Act
        var resp = bookService.update(bookDTO, bookDTO.getIsbn());

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(bookDTO.getIsbn(), resp.getIsbn());
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
        verify(bookRepository, times(1)).save(any(BookEntity.class));
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
    }

    @Test
    void updateBookWhenBookNotFoundTest(){
        //Arrange
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("123");

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());
        //Act
        var resp = Assertions.assertThrows(BookNotFoundException.class, () -> bookService.update(bookDTO, bookDTO.getIsbn()));

        //Assert
        Assertions.assertNotNull(resp);
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
    }

    @Test
    void deleteBookTest(){
        //Arrange
        String isbn = "123";

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        //Act
        bookService.delete(isbn);

        //Assert
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
    }

    @Test
    void deleteBookWhenBookNotFoundTest(){
        //Arrange
        String isbn = "123";

        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());
        //Act
        var resp = Assertions.assertThrows(BookNotFoundException.class, () -> bookService.delete(isbn));

        //Assert
        Assertions.assertNotNull(resp);
        verify(bookRepository, times(1)).getByIsbn(any(String.class));
    }

    @Test
    void findAllBooksTest(){
        //Arrange
        int expectedSize = 1;
        ArrayList<BookDTO> books = new ArrayList<>();
        books.add(new BookDTO());

        ArrayList<BookEntity> bookEntities = new ArrayList<>();
        bookEntities.add(new BookEntity());

        when(bookRepository.getAllByDeleted_False()).thenReturn(bookEntities);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(books.get(0));
        //Act
        var resp = bookService.findAll();
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(bookRepository, times(1)).getAllByDeleted_False();
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
    }

    @Test
    void findAllBooksEmptyTest(){
        //Arrange
        int expectedSize = 0;

        when(bookRepository.getAllByDeleted_False()).thenReturn(new ArrayList<>());

        //Act
        var resp = bookService.findAll();

        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(bookRepository, times(1)).getAllByDeleted_False();
    }

    @Test
    void getAllBooksByUserIdTest(){
        //Arrange
        int id = 1;
        int expectedSize = 1;

        ArrayList<BookDTO> books = new ArrayList<>();
        books.add(new BookDTO());
        ArrayList<BookEntity> bookEntities = new ArrayList<>();
        bookEntities.add(new BookEntity());

        when(bookRepository.getAllBooksByUser(any(Integer.class))).thenReturn(bookEntities);
        when(bookMapper.toDTO(any(BookEntity.class))).thenReturn(books.get(0));

        //Act
        var resp = bookService.getAllBooksByUserId(id);
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(bookRepository, times(1)).getAllBooksByUser(any(Integer.class));
        verify(bookMapper, times(1)).toDTO(any(BookEntity.class));
    }

    @Test
    void getAllBooksByUserIdEmptyTest(){
        //Arrange
        int id = 1;
        int expectedSize = 0;


        when(bookRepository.getAllBooksByUser(any(Integer.class))).thenReturn(new ArrayList<>());

        //Act
        var resp = bookService.getAllBooksByUserId(id);
        //Assert
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(expectedSize, resp.size());
        verify(bookRepository, times(1)).getAllBooksByUser(any(Integer.class));
    }
}
