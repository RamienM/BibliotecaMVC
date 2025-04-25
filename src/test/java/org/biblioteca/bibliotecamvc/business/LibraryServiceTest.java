package org.biblioteca.bibliotecamvc.business;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookIsBookedException;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.borrow.BorrowNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyExistException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyHaveThatBook;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.log.LogNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyBookedThisBookException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
import org.biblioteca.bibliotecamvc.business.mapper.LibraryMapper;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.persistence.entities.*;
import org.biblioteca.bibliotecamvc.persistence.repository.*;
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
public class LibraryServiceTest {

    @Mock
    private LibraryJPARepository libraryRepository;

    @Mock
    private BookJPARepository bookRepository;

    @Mock
    private LogJPARepository logRepository;

    @Mock
    private UserJPARepository userRepository;

    @Mock
    private BorrowJPARepository borrowRepository;

    @Mock
    private LibraryMapper libraryMapper;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    void saveLibraryTest() {
        //Arrange
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(libraryRepository.save(any(LibraryEntity.class))).thenReturn(new LibraryEntity());
        when(libraryMapper.toEntity(any(LibraryDTO.class))).thenReturn(new LibraryEntity());
        when(libraryMapper.toDTO(any(LibraryEntity.class))).thenReturn(libraryDTO);

        //Act
        var resp = libraryService.save(libraryDTO);

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findByName(any(String.class));
        verify(libraryRepository, times(1)).save(any(LibraryEntity.class));
        verify(libraryMapper, times(1)).toDTO(any(LibraryEntity.class));
        verify(libraryMapper, times(1)).toEntity(any(LibraryDTO.class));

    }

    @Test
    void saveLibraryWhenLibraryAlreadyExistsTest() {
        //Arrange
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryRepository.findByName(any(String.class))).thenReturn(Optional.of(new LibraryEntity()));

        //Act
        var resp = Assertions.assertThrows(LibraryAlreadyExistException.class, () -> libraryService.save(libraryDTO));

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findByName(any(String.class));

    }

    @Test
    void findLibraryByIdTest() {
        //Arrange
        int id = 1;

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));
        when(libraryMapper.toDTO(any(LibraryEntity.class))).thenReturn(new LibraryDTO());

        //Act
        var resp = libraryService.findById(id);

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
        verify(libraryMapper,times(1)).toDTO(any(LibraryEntity.class));
    }

    @Test
    void findLibraryByIdWhenLibraryNotFoundTest() {
        //Arrange
        int id = 1;

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(LibraryNotFoundException.class, () -> libraryService.findById(id));

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void updateLibraryTest() {
        //Arrange
        int id = 1;
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));
        when(libraryRepository.save(any(LibraryEntity.class))).thenReturn(new LibraryEntity());
        when(libraryMapper.toDTO(any(LibraryEntity.class))).thenReturn(new LibraryDTO());

        //Assert
        var resp = libraryService.update(libraryDTO,id);

        //Act
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
        verify(libraryRepository,times(1)).save(any(LibraryEntity.class));
        verify(libraryMapper,times(1)).toDTO(any(LibraryEntity.class));
    }

    @Test
    void updateLibraryWhenLibraryNotFoundTest() {
        //Arrange
        int id = 1;
        LibraryDTO libraryDTO = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        //Assert
        var resp = Assertions.assertThrows(LibraryNotFoundException.class, () -> libraryService.update(libraryDTO,id));

        //Act
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void deleteLibraryTest() {
        //Arrange
        int id = 1;

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));

        //Assert
        libraryService.delete(id);

        //Act
        verify(libraryRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void deleteLibraryWhenLibraryNotFoundTest() {
        //Arrange
        int id = 1;

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        //Assert
        var resp = Assertions.assertThrows(LibraryNotFoundException.class, () -> libraryService.delete(id));

        //Act
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void findAllLibrariesTest() {
        //Arrange
        ArrayList<LibraryDTO> libraryDTOs = new ArrayList<>();
        libraryDTOs.add(new LibraryDTO());

        ArrayList<LibraryEntity> libraryEntities = new ArrayList<>();
        libraryEntities.add(new LibraryEntity());

        when(libraryRepository.getAllByDeleted_False()).thenReturn(libraryEntities);
        when(libraryMapper.toDTO(any(LibraryEntity.class))).thenReturn(libraryDTOs.get(0));

        int expectedSize = 1;

        //Act
        var resp = libraryService.findAll();

        //Assert
        assertThat(resp).isNotNull();
        Assertions.assertEquals(expectedSize, resp.size());
        verify(libraryRepository,times(1)).getAllByDeleted_False();
        verify(libraryMapper,times(1)).toDTO(any(LibraryEntity.class));
    }

    @Test
    void findAllLibrariesEmptyTest() {
        //Arrange

        when(libraryRepository.getAllByDeleted_False()).thenReturn(new ArrayList<>());

        int expectedSize = 0;

        //Act
        var resp = libraryService.findAll();

        //Assert
        assertThat(resp).isNotNull();
        Assertions.assertEquals(expectedSize, resp.size());
        verify(libraryRepository,times(1)).getAllByDeleted_False();
    }

    @Test
    void addBookTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));
        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.empty());
        when(logRepository.save(any(LogEntity.class))).thenReturn(new LogEntity());

        //Act
        libraryService.addBook(id,isbn);

        //Assert
        verify(libraryRepository,times(1)).findById(any(Integer.class));
        verify(bookRepository,times(1)).getByIsbn(any(String.class));
        verify(logRepository,times(1)).save(any(LogEntity.class));
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void addBookWhenLibraryNotFoundTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(LibraryNotFoundException.class, () -> libraryService.addBook(id,isbn));

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void addBookWhenBookNotFoundTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));
        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(BookNotFoundException.class, () -> libraryService.addBook(id,isbn));

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
        verify(bookRepository,times(1)).getByIsbn(any(String.class));

    }

    @Test
    void addBookWhenLibraryAlreadyHaveThatBookTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(libraryRepository.findById(any(Integer.class))).thenReturn(Optional.of(new LibraryEntity()));
        when(bookRepository.getByIsbn(any(String.class))).thenReturn(Optional.of(new BookEntity()));
        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new LogEntity()));

        //Act
        var resp = Assertions.assertThrows(LibraryAlreadyHaveThatBook.class, () -> libraryService.addBook(id,isbn));

        //Assert
        assertThat(resp).isNotNull();
        verify(libraryRepository,times(1)).findById(any(Integer.class));
        verify(bookRepository,times(1)).getByIsbn(any(String.class));
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void deleteBookTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new LogEntity()));

        //Act
        libraryService.deleteBook(id,isbn);

        //Assert
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void deleteBookWhenLogNotFoundTest() {
        //Arrange
        int id = 1;
        String isbn = "123";

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(LogNotFoundException.class, () -> libraryService.deleteBook(id,isbn));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void borrowBookTest() {
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new LogEntity()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new UserEntity()));
        when(borrowRepository.findByUser_idAndLog_BookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.empty());
        when(borrowRepository.save(any(BorrowEntity.class))).thenReturn(new BorrowEntity());
        when(logRepository.save(any(LogEntity.class))).thenReturn(new LogEntity());

        //Act
        libraryService.borrowBook(libraryId,isbn,userId);

        //Assert
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(userRepository,times(1)).findById(any(Integer.class));
        verify(borrowRepository,times(1)).findByUser_idAndLog_BookEntity_Isbn(any(Integer.class),any(String.class));
        verify(borrowRepository,times(1)).save(any(BorrowEntity.class));
        verify(logRepository,times(1)).save(any(LogEntity.class));
    }

    @Test
    void borrowBookWhenLogNotFoundTest() {
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(LogNotFoundException.class, () -> libraryService.borrowBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void borrowBookWhenUserNotFoundTest() {
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new LogEntity()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        //Act
        var resp = Assertions.assertThrows(UserNotFoundException.class, () -> libraryService.borrowBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(userRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void borrowBookWhenBookIsBookedTest() {
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        LogEntity logEntity = new LogEntity();
        logEntity.setBooked(true);

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(logEntity));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new UserEntity()));

        //Act
        var resp = Assertions.assertThrows(BookIsBookedException.class, () -> libraryService.borrowBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(userRepository,times(1)).findById(any(Integer.class));
    }

    @Test
    void borrowBookWhenUserAlreadyBookedThisBookTest() {
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new LogEntity()));
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new UserEntity()));
        when(borrowRepository.findByUser_idAndLog_BookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(new BorrowEntity()));

        //Act
        var resp = Assertions.assertThrows(UserAlreadyBookedThisBookException.class, () -> libraryService.borrowBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(userRepository,times(1)).findById(any(Integer.class));
        verify(borrowRepository,times(1)).findByUser_idAndLog_BookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void returnBookTest(){
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        LogEntity log = new LogEntity();
        log.setId(1);

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(log));
        when(borrowRepository.findByUser_IdAndLog_Id(any(Integer.class),any(Integer.class))).thenReturn(Optional.of(new BorrowEntity()));
        when(logRepository.save(any(LogEntity.class))).thenReturn(new LogEntity());

        //Act
        libraryService.returnBook(libraryId,isbn,userId);

        //Assert
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(borrowRepository,times(1)).findByUser_IdAndLog_Id(any(Integer.class),any(Integer.class));
        verify(logRepository, times(1)).save(any(LogEntity.class));
    }

    @Test
    void returnBookWhenLogNotFoundTest(){
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(LogNotFoundException.class, () -> libraryService.returnBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
    }

    @Test
    void returnBookWhenBorrowNotFoundTest(){
        //Arrange
        int libraryId = 1;
        String isbn = "123";
        int userId = 1;

        LogEntity log = new LogEntity();
        log.setId(1);

        when(logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class))).thenReturn(Optional.of(log));
        when(borrowRepository.findByUser_IdAndLog_Id(any(Integer.class),any(Integer.class))).thenReturn(Optional.empty());

        //Act
        var resp = Assertions.assertThrows(BorrowNotFoundException.class, () -> libraryService.returnBook(libraryId,isbn,userId));

        //Assert
        assertThat(resp).isNotNull();
        verify(logRepository,times(1)).findByLibraryEntity_IdAndBookEntity_Isbn(any(Integer.class),any(String.class));
        verify(borrowRepository,times(1)).findByUser_IdAndLog_Id(any(Integer.class),any(Integer.class));
    }
}
