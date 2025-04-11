package org.biblioteca.bibliotecamvc.business.service;

import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyHaveThatBook;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryNotFoundException;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.mapper.LibraryMapper;
import org.biblioteca.bibliotecamvc.business.service.interfaces.BasicCRUD;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.entities.LibraryEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.BookJPARepository;
import org.biblioteca.bibliotecamvc.persistence.repository.LibraryJPARepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryService implements BasicCRUD<LibraryDTO, Integer> {
    private final LibraryJPARepository libraryRepository;
    private final BookJPARepository bookRepository;

    private final LibraryMapper libraryMapper;
    private final BookMapper bookMapper;
    private final LibraryJPARepository libraryJPARepository;


    @Override
    public List<LibraryDTO> findAll() {
        List<LibraryDTO> libraryDTOs = new ArrayList<>();
        libraryRepository.getAllByDeleted_False().forEach(library -> libraryDTOs.add(libraryMapper.toDTO(library)));
        return libraryDTOs;
    }

    @Override
    public LibraryDTO findById(Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        return libraryMapper.toDTO(libraryEntity.get());
    }

    //TODO se tiene que cambiar
    public List<BookDTO> getAllBooksAvailableById(Integer id) {
        List<BookDTO> bookEntities = new ArrayList<>();
        libraryRepository.getAllBooksAvailableById(id).forEach(book -> bookEntities.add(bookMapper.toDTO(book)));
        return  bookEntities;
    }

    //TODO se tiene que cambiar (obtener todos los libros)
    public List<BookDTO> getAllBooksById(Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity library = libraryEntity.get();
        List<BookDTO> bookDTOs = new ArrayList<>();
        library.getBookEntities().forEach(entity -> bookDTOs.add(bookMapper.toDTO(entity)));
        return  bookDTOs;
    }

    @Override
    public LibraryDTO save(LibraryDTO libraryDTO) {
        return libraryMapper.toDTO(libraryRepository.save(libraryMapper.toEntity(libraryDTO)));
    }


    @Override
    public LibraryDTO update(LibraryDTO libraryDTO, Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity oldLibrary = libraryEntity.get();
        oldLibrary.setName(libraryDTO.getName()==null?oldLibrary.getName():libraryDTO.getName());
        return libraryMapper.toDTO(libraryRepository.save(oldLibrary));
    }


    @Override
    public void delete(Integer id) {
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity oldLibrary = libraryEntity.get();
        oldLibrary.setDeleted(true);
        libraryRepository.save(oldLibrary);
    }

    //TODO Se tiene que cambiar
    public void addBook(Integer id, String isbn){
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(isbn);
        if (bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        LibraryEntity library = libraryEntity.get();
        if (library.getBookEntities().contains(bookEntity.get())) throw new LibraryAlreadyHaveThatBook("Library already have that book");
        library.getBookEntities().add(bookEntity.get());
        libraryJPARepository.save(library);
    }
    //TODO Se tiene que cambiar
    public void deleteBook(Integer id, String isbn){
        Optional<LibraryEntity> libraryEntity = libraryRepository.findById(id);
        if (libraryEntity.isEmpty()) throw new LibraryNotFoundException("Library not found");
        LibraryEntity library = libraryEntity.get();
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(isbn);
        if(bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        BookEntity book = bookEntity.get();
        library.getBookEntities().remove(book);
        libraryJPARepository.save(library);
    }
}
