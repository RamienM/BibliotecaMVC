package org.biblioteca.bibliotecamvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.dto.LibraryDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryAlreadyExistException;
import org.biblioteca.bibliotecamvc.business.exception.library.LibraryNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.log.LogNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.business.service.LogService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LibraryController.class)
public class LibraryControllerTest {

    @MockitoBean
    private LibraryService libraryService;
    @MockitoBean
    private LogService logService;
    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void viewAdminViewGetTest() throws Exception {
        //Arrange
        ArrayList<LibraryDTO> libraries = new ArrayList<>();

        when(libraryService.findAll()).thenReturn(libraries);
        //Act

        ResultActions resp = mockMvc.perform(get("/library/libraryMain"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/libraryMain"))
                .andExpect(model().attribute("libraries", libraries));
        verify(libraryService,times(1)).findAll();
    }

    @Test
    void viewAdminViewPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act

        ResultActions resp = mockMvc.perform(post("/library/libraryMain"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void enterLibraryMainGetTest() throws Exception {
        //Arrange
        int id = 1;
        ArrayList<BookDTO> books = new ArrayList<>();

        when(logService.getAllBooksAvailableByLibraryId(any(Integer.class))).thenReturn(books);
        //Act

        ResultActions resp = mockMvc.perform(get("/library/enter/enterLibraryMain/{id}", id));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/enter/enterLibraryMain"))
                .andExpect(model().attribute("books", books));
        verify(logService,times(1)).getAllBooksAvailableByLibraryId(any(Integer.class));
    }

    @Test
    void enterLibraryMainPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        int id = 1;

        //Act

        ResultActions resp = mockMvc.perform(post("/library/enter/enterLibraryMain/{id}", id));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void saveLibraryGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/library/saveLibrary"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/saveLibrary"))
                .andExpect(model().attributeExists("LibraryDTO"));
    }

    @Test
    void saveLibraryPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        LibraryDTO libraryDTO  = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryService.save(any(LibraryDTO.class))).thenReturn(libraryDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/library/saveLibrary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(libraryDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/libraryMain"));

        verify(libraryService,times(1)).save(any(LibraryDTO.class));
    }

    @Test
    void saveLibraryPostWhenLibraryAlreadyExistTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        LibraryDTO libraryDTO  = new LibraryDTO();
        libraryDTO.setName("Test Library");

        when(libraryService.save(any(LibraryDTO.class))).thenThrow(LibraryAlreadyExistException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/library/saveLibrary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(libraryDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/libraryMain"));

        verify(libraryService,times(1)).save(any(LibraryDTO.class));
    }

    @Test
    void addBookGetTest() throws Exception {
        //Arrange
        ArrayList<BookDTO> books = new ArrayList<>();

        when(bookService.findAll()).thenReturn(books);
        //Act
        ResultActions resp = mockMvc.perform(get("/library/enter/addBook"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/enter/addBook"))
                .andExpect(model().attributeExists("BookDTO"))
                .andExpect(model().attribute("libros", books));


        verify(bookService,times(1)).findAll();
    }

    @Test
    void addBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;

        BookDTO bookDTO  = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn("123");
        bookDTO.setAuthor("Test Author");

        doNothing().when(libraryService).addBook(any(Integer.class),any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/library/enter/addBook")
                .sessionAttr("LibraryID",id)
                .param("title", bookDTO.getTitle())
                .param("isbn", bookDTO.getIsbn())
                .param("author", bookDTO.getAuthor()));


        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+id));

        verify(libraryService,times(1)).addBook(any(Integer.class),any(String.class));
    }

    @Test
    void addBookPostWhenLibraryNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;

        BookDTO bookDTO  = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn("123");
        bookDTO.setAuthor("Test Author");

        doThrow(LibraryNotFoundException.class).when(libraryService).addBook(any(Integer.class),any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/library/enter/addBook")
                .sessionAttr("LibraryID",id)
                .param("title", bookDTO.getTitle())
                .param("isbn", bookDTO.getIsbn())
                .param("author", bookDTO.getAuthor()));


        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+id));

        verify(libraryService,times(1)).addBook(any(Integer.class),any(String.class));
    }

    @Test
    void addBookPostWhenBookNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;

        BookDTO bookDTO  = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn("123");
        bookDTO.setAuthor("Test Author");

        doThrow(BookNotFoundException.class).when(libraryService).addBook(any(Integer.class),any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/library/enter/addBook")
                .sessionAttr("LibraryID",id)
                .param("title", bookDTO.getTitle())
                .param("isbn", bookDTO.getIsbn())
                .param("author", bookDTO.getAuthor()));


        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+id));

        verify(libraryService,times(1)).addBook(any(Integer.class),any(String.class));
    }

    @Test
    void addBookPostWhenLibraryAlreadyHaveThatBookTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;

        BookDTO bookDTO  = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setIsbn("123");
        bookDTO.setAuthor("Test Author");

        doThrow(LibraryNotFoundException.class).when(libraryService).addBook(any(Integer.class),any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/library/enter/addBook")
                .sessionAttr("LibraryID",id)
                .param("title", bookDTO.getTitle())
                .param("isbn", bookDTO.getIsbn())
                .param("author", bookDTO.getAuthor()));


        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+id));

        verify(libraryService,times(1)).addBook(any(Integer.class),any(String.class));
    }

    @Test
    void updateLibraryGetTest() throws Exception {
        //Arrange
        int id = 1;
        LibraryDTO libraryDTOs = new LibraryDTO();

        when(libraryService.findById(any(Integer.class))).thenReturn(libraryDTOs);

        //Act
        ResultActions resp = mockMvc.perform(get("/library/updateLibrary/{id}", id));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/updateLibrary"))
                .andExpect(model().attributeExists("LibraryDTO"));
        verify(libraryService,times(1)).findById(any(Integer.class));
    }

    @Test
    void updateLibraryGetWhenLibraryNotFoundTest() throws Exception {
        //Arrange
        int id = 1;

        when(libraryService.findById(any(Integer.class))).thenThrow(LibraryNotFoundException.class);

        //Act
        ResultActions resp = mockMvc.perform(get("/library/updateLibrary/{id}", id));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/library/updateLibrary"))
                .andExpect(model().attributeExists("LibraryDTO"));
        verify(libraryService,times(1)).findById(any(Integer.class));
    }

    @Test
    void updateLibraryPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;
        LibraryDTO libraryDTO  = new LibraryDTO();
        libraryDTO.setName("Test Book");

        when(libraryService.update(any(LibraryDTO.class),any(Integer.class))).thenReturn(libraryDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/library/updateLibrary/{id}",id)
                .param("name",libraryDTO.getName()));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/libraryMain"));
        verify(libraryService,times(1)).update(any(LibraryDTO.class),any(Integer.class));
    }

    @Test
    void updateLibraryPostWhenLibraryNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;
        LibraryDTO libraryDTO  = new LibraryDTO();

        when(libraryService.update(any(LibraryDTO.class),any(Integer.class))).thenThrow(LibraryNotFoundException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/library/updateLibrary/{id}",id)
                .param("name",libraryDTO.getName()));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/libraryMain"));
        verify(libraryService,times(1)).update(any(LibraryDTO.class),any(Integer.class));
    }

    @Test
    void deleteLibraryGetTest() throws Exception {
        //Arrange
        int id = 1;
        int expectedStatus = 302;

        doNothing().when(libraryService).delete(any(Integer.class));
        //Act
        ResultActions resp = mockMvc.perform(get("/library/deleteLibrary/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus))
            .andExpect(view().name("redirect:/library/libraryMain"));
        verify(libraryService,times(1)).delete(any(Integer.class));
    }

    @Test
    void deleteLibraryGetWhenLibraryNotFoundTest() throws Exception {
        //Arrange
        int id = 1;
        int expectedStatus = 302;

        doThrow(LibraryNotFoundException.class).when(libraryService).delete(any(Integer.class));
        //Act
        ResultActions resp = mockMvc.perform(get("/library/deleteLibrary/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/libraryMain"));
        verify(libraryService,times(1)).delete(any(Integer.class));
    }

    @Test
    void deleteLibraryPostTest() throws Exception {
        //Arrange
        int id = 1;
        int expecteStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/library/deleteLibrary/{id}",id));

        //Assert
        resp.andExpect(status().is(expecteStatus));
    }

    @Test
    void deleteLibraryEnterGetTest() throws Exception {
        //Arrange
        String id = "1";
        int libraryId = 1;
        int expectedStatus = 302;

        doNothing().when(libraryService).deleteBook(any(Integer.class), any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/library/deleteBook/{id}",id)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+libraryId));

        verify(libraryService,times(1)).deleteBook(any(Integer.class), any(String.class));
    }

    @Test
    void deleteLibraryEnterGetWhenLogNotFoundTest() throws Exception {
        //Arrange
        String id = "1";
        int libraryId = 1;
        int expectedStatus = 302;

        doThrow(LogNotFoundException.class).when(libraryService).deleteBook(any(Integer.class),any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/library/deleteBook/{id}",id)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/library/enter/enterLibraryMain/"+libraryId));

        verify(libraryService,times(1)).deleteBook(any(Integer.class), any(String.class));
    }

    @Test
    void deleteLibraryEnterPostTest() throws Exception {
        //Arrange
        String id = "1";
        int libraryId = 1;
        int expecteStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/library/deleteBook/{id}",id)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expecteStatus));
    }
}
