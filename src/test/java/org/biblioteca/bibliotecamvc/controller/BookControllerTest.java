package org.biblioteca.bibliotecamvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biblioteca.bibliotecamvc.business.dto.BookDTO;
import org.biblioteca.bibliotecamvc.business.exception.book.BookAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.book.BookNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
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
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {

    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void viewAdminViewGetTest() throws Exception {
        //Arrange
        ArrayList<BookDTO> books = new ArrayList<>();

        when(bookService.findAll()).thenReturn(books);
        //Act

        ResultActions resp = mockMvc.perform(get("/book/bookMain"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/book/bookMain"))
                .andExpect(model().attribute("books", books));
        verify(bookService,times(1)).findAll();
    }

    @Test
    void viewAdminViewPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act

        ResultActions resp = mockMvc.perform(post("/book/bookMain"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void saveBookGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/book/saveBook"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/book/saveBook"))
                .andExpect(model().attributeExists("BookDTO"));
    }

    @Test
    void saveBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Book Title");
        bookDTO.setAuthor("Author");

        when(bookService.save(any(BookDTO.class))).thenReturn(bookDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/book/saveBook").contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper.writeValueAsString(bookDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).save(any(BookDTO.class));
    }

    @Test
    void saveBookPostWhenBookAlreadyExistTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Book Title");
        bookDTO.setAuthor("Author");

        when(bookService.save(any(BookDTO.class))).thenThrow(BookAlreadyExistsException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/book/saveBook").contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper.writeValueAsString(bookDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).save(any(BookDTO.class));
    }

    @Test
    void updateBookGetTest() throws Exception {
        //Arrange
        String id = "1";

        when(bookService.findById(any(String.class))).thenReturn(new BookDTO());

        //Act
        ResultActions resp = mockMvc.perform(get("/book/updateBook/{id}",id));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/book/updateBook"))
                .andExpect(model().attributeExists("BookDTO"));

        verify(bookService,times(1)).findById(any(String.class));
    }

    @Test
    void updateBookGetWhenBookNotFoundTest() throws Exception {
        //Arrange
        String id = "1";
        int expectedStatus = 302;

        when(bookService.findById(any(String.class))).thenThrow(BookNotFoundException.class);

        //Act
        ResultActions resp = mockMvc.perform(get("/book/updateBook/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).findById(any(String.class));
    }

    @Test
    void updateBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Book Title");
        bookDTO.setAuthor("Author");

        when(bookService.update(any(BookDTO.class),any(String.class))).thenReturn(bookDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/book/updateBook/{id}",id).contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper.writeValueAsString(bookDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).update(any(BookDTO.class),any(String.class));
    }

    @Test
    void updateBookPostWhenBookNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Book Title");
        bookDTO.setAuthor("Author");

        when(bookService.update(any(BookDTO.class),any(String.class))).thenThrow(BookNotFoundException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/book/updateBook/{id}",id).contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper.writeValueAsString(bookDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).update(any(BookDTO.class),any(String.class));
    }


    @Test
    void deleteBookGetTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";

        doNothing().when(bookService).delete(anyString());

        //Act
        ResultActions resp = mockMvc.perform(get("/book/deleteBook/{id}",id));
        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).delete(id);
    }

    @Test
    void deleteBookGetWhenBookNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";

        doThrow(BookNotFoundException.class).when(bookService).delete(id);

        //Act
        ResultActions resp = mockMvc.perform(get("/book/deleteBook/{id}",id));
        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/book/bookMain"));

        verify(bookService,times(1)).delete(id);
    }

    @Test
    void deleteBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        String id = "1";

        //Act

        ResultActions resp = mockMvc.perform(post("/book/deleteBook/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

}
