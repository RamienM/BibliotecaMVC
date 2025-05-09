package org.biblioteca.bibliotecamvc.business.service;

import lombok.AllArgsConstructor;
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
import org.biblioteca.bibliotecamvc.business.service.interfaces.BasicCRUD;
import org.biblioteca.bibliotecamvc.persistence.entities.*;
import org.biblioteca.bibliotecamvc.persistence.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryService implements BasicCRUD<LibraryDTO, Integer> {
    private final LibraryJPARepository libraryRepository;
    private final BookJPARepository bookRepository;
    private final LogJPARepository logRepository;
    private final LibraryMapper libraryMapper;
    private final UserJPARepository userJPARepository;
    private final BorrowJPARepository borrowJPARepository;

    /**
     * Permite la obtención de todas la bibliotecas, se excluyen las "eliminadas".
     * @return Devuelve una lista de bibliotecas
     */
    @Override
    public List<LibraryDTO> findAll() {
        List<LibraryDTO> libraryDTOs = new ArrayList<>();
        libraryRepository.getAllByDeleted_False().forEach(library -> libraryDTOs.add(libraryMapper.toDTO(library)));
        return libraryDTOs;
    }

    /**
     * Permite la obtención de una biblioteca concreta.
     * @param id    ID de la biblioteca
     * @return  Devuelve la biblioteca solicitada
     */
    @Override
    public LibraryDTO findById(Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        return libraryMapper.toDTO(libraryEntity.get());
    }

    /**
     * Permite añadir una biblioteca nueva.
     * @param libraryDTO Información de la biblioteca
     * @return Devuelve la biblioteca guardada
     */
    @Override
    public LibraryDTO save(LibraryDTO libraryDTO) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findByName(libraryDTO.getName());
        if (libraryEntity.isPresent()) throw new LibraryAlreadyExistException("Library already exists");
        return libraryMapper.toDTO(libraryRepository.save(libraryMapper.toEntity(libraryDTO)));
    }

    /**
     * Permite la actualización de una biblioteca concreta.
     * @param libraryDTO Información a actualizar de la biblioteca
     * @param id ID de la biblioteca a actualizar
     * @return Devuelve la biblioteca acutalizada
     */
    @Override
    public LibraryDTO update(LibraryDTO libraryDTO, Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity oldLibrary = libraryEntity.get();
        oldLibrary.setName(libraryDTO.getName()==null?oldLibrary.getName():libraryDTO.getName());
        return libraryMapper.toDTO(libraryRepository.save(oldLibrary));
    }

    /**
     * Permite la eliminación de una biblioteca concreta
     * @param id ID de la biblioteca a eliminar
     */
    @Override
    public void delete(Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity oldLibrary = libraryEntity.get();
        oldLibrary.setDeleted(true);
        libraryRepository.save(oldLibrary);
    }

    /**
     * Permite añadir un libro concreto a una biblioteca. Se controla que el libro no se encuentre ya en esa biblioteca.
     * @param id    ID de la biblioteca
     * @param isbn  ISBN del libro
     */
    public void addBook(Integer id, String isbn){
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(isbn);
        if (bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        LibraryEntity library = libraryEntity.get();
        Optional<LogEntity> logEntity = logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(id, isbn);
        if(logEntity.isPresent()) throw new LibraryAlreadyHaveThatBook("Library already have that book");
        LogEntity log = new LogEntity();
        log.setLibraryEntity(library);
        log.setBookEntity(bookEntity.get());
        logRepository.save(log);
    }

    /**
     * Permite eliminar un libro concreto de una biblioteca.
     * @param id ID de la biblioteca
     * @param isbn ISBN de la biblioteca
     */
    public void deleteBook(Integer id, String isbn){
        Optional<LogEntity> logEntity = logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(id, isbn);
        if(logEntity.isEmpty()) throw new LogNotFoundException("Log not found");
        LogEntity log = logEntity.get();
        log.setDeleted(true);
        logRepository.save(log);
    }

    /**
     * Permite a un usuario concreto reservar un libro de una biblioteca.
     * Se controla que el libro no se encuentre reservado y que el usuario no haya reservado el libro con anterioridad
     * @param libraryId ID de la libreria
     * @param isbn  ISBN del libro
     * @param userId ID del usuario
     */
    public void borrowBook(Integer libraryId, String isbn, Integer userId){
        Optional<LogEntity> logEntity = logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(libraryId,isbn);
        if (logEntity.isEmpty()) throw new LogNotFoundException("Log not found");
        Optional<UserEntity> userEntity = userJPARepository.findById(userId);
        if (userEntity.isEmpty()) throw new UserNotFoundException("User not found");
        LogEntity log = logEntity.get();
        if (log.getBooked()) throw new BookIsBookedException("Book is already borrowed");
        UserEntity user = userEntity.get();
        Optional<BorrowEntity> borrowEntity = borrowJPARepository.findByUser_idAndLog_BookEntity_Isbn(userId,isbn);
        if(borrowEntity.isPresent()) throw new UserAlreadyBookedThisBookException("User already booked this book before");
        BorrowEntity entity = new BorrowEntity();
        entity.setLog(log);
        entity.setUser(user);
        borrowJPARepository.save(entity);
        log.setBooked(true);
        logRepository.save(log);
    }

    /**
     * Permite a un usuario devolver un libro de una biblioteca.
     * @param libraryId ID de la biblioteca
     * @param isbn ISBN del libro
     * @param userId ID del usuario
     */
    public void returnBook(Integer libraryId, String isbn, Integer userId){
        Optional<LogEntity> logEntity = logRepository.findByLibraryEntity_IdAndBookEntity_Isbn(libraryId,isbn);
        if (logEntity.isEmpty()) throw new LogNotFoundException("Log not found");
        Optional<BorrowEntity> borrowEntity = borrowJPARepository.findByUser_IdAndLog_Id(userId,logEntity.get().getId());
        if (borrowEntity.isEmpty()) throw new BorrowNotFoundException("Borrow not found");
        BorrowEntity borrow = borrowEntity.get();
        borrow.setReturned(true);
        LogEntity log = logEntity.get();
        log.setBooked(false);
        logRepository.save(log);
    }
}
