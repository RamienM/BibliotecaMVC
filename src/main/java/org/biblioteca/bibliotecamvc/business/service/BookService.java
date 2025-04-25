package org.biblioteca.bibliotecamvc.business.service;

import lombok.AllArgsConstructor;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.mapper.BookMapper;
import org.biblioteca.bibliotecamvc.business.service.interfaces.BasicCRUD;
import org.biblioteca.bibliotecamvc.persistence.entities.BookEntity;
import org.biblioteca.bibliotecamvc.persistence.repository.BookJPARepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService implements BasicCRUD<BookDTO,String> {
    private final BookJPARepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Obtención de todos los libros, no se incluyen los libros "eliminados".
     * @return Lista de libros
     */
    @Override
    public List<BookDTO> findAll() {
        List<BookDTO> bookDTOs = new ArrayList<>();
        bookRepository.getAllByDeleted_False().forEach(book -> bookDTOs.add(bookMapper.toDTO(book)));
        return bookDTOs;
    }

    /**
     * Devuelve todos los libros que tienen en posesión un usuario.
     * @param user_id ID del usuario
     * @return Lista de libros en posesion
     */
    public List<BookDTO> getAllBooksByUserId(Integer user_id) {
        List<BookDTO> books = new ArrayList<>();
        bookRepository.getAllBooksByUser(user_id).forEach(book -> books.add(bookMapper.toDTO(book)));
        return books;
    }

    /**
     * Se obtiene un libro en concreto.
     * @param id ISBN del libro
     * @return Devuelve el libro
     */
    @Override
    public BookDTO findById(String id) {
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(id);
        if (bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        return bookMapper.toDTO(bookEntity.get());
    }

    /**
     * Permite la creación de nuevos libros. Se comprueba que el libro no exista ya en la biblioteca.
     * @param book Libro que se desea añadir
     * @return Libro guardado
     */
    @Override
    public BookDTO save(BookDTO book) {
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(book.getIsbn());
        if (bookEntity.isPresent()) throw new BookAlreadyExistsException("Book already exists");
        BookEntity bookToEntity = bookMapper.toEntity(book);
        BookEntity savedEntity = bookRepository.save(bookToEntity);
        return bookMapper.toDTO(savedEntity);
    }

    /**
     * Permite la actualización de un libro concreto.
     * @param book Datos que se desea actualizar.
     * @param id ISBN del libro a actualizar
     * @return Devuelve el libro actualizado
     */
    @Override
    public BookDTO update(BookDTO book, String id) {
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(id);
        if (bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        BookEntity oldBook = bookEntity.get();
        oldBook.setIsbn(book.getIsbn() == null ? oldBook.getIsbn() : book.getIsbn());
        oldBook.setTitle(book.getTitle() == null ? oldBook.getTitle() : book.getTitle());
        oldBook.setAuthor(book.getAuthor() == null ? oldBook.getAuthor() : book.getAuthor());
        return bookMapper.toDTO(bookRepository.save(oldBook));
    }

    /**
     * Simula la eliminación de un libro.
     * @param id ISBN del libro a eliminar
     */
    @Override
    public void delete(String id) {
        Optional<BookEntity> bookEntity = bookRepository.getByIsbn(id);
        if (bookEntity.isEmpty()) throw new BookNotFoundException("Book not found");
        BookEntity oldBook = bookEntity.get();
        oldBook.setDeleted(true);
        bookRepository.save(oldBook);
    }
}
