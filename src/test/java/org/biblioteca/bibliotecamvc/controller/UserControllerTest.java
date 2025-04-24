package org.biblioteca.bibliotecamvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biblioteca.bibliotecamvc.business.dto.*;
import org.biblioteca.bibliotecamvc.business.exception.book.BookIsBookedException;
import org.biblioteca.bibliotecamvc.business.exception.borrow.BorrowNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.log.LogNotFoundException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyBookedThisBookException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserAlreadyExistsException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
import org.biblioteca.bibliotecamvc.business.service.BookService;
import org.biblioteca.bibliotecamvc.business.service.LibraryService;
import org.biblioteca.bibliotecamvc.business.service.LogService;
import org.biblioteca.bibliotecamvc.business.service.UserService;
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
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @MockitoBean
    private UserService userService;
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
        ArrayList<UserDTO> users = new ArrayList<>();

        when(userService.findAll()).thenReturn(users);

        //Act
        ResultActions resp = mockMvc.perform(get("/user/admin/userAdminMain"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(model().attribute("users",users))
                .andExpect(view().name("/user/admin/userAdminMain"));
        verify(userService,times(1)).findAll();
    }

    @Test
    void viewAdminViewPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/userAdminMain"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void saveUserAdminMainGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/user/admin/saveUser"));

        //Assert
        resp.andExpect(status().isOk()).andExpect(view().name("/user/admin/saveUser")).andExpect(model().attributeExists( "UserRegisterDTO" ));
    }

    @Test
    void saveUserAdminMainPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("admin");

        doNothing().when(userService).register(any(UserRegisterDTO.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/saveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/admin/userAdminMain"));
        verify(userService,times(1)).register(any(UserRegisterDTO.class));
    }

    @Test
    void saveUserAdminMainPostWhenUserAlreadyExistsTest() throws Exception {
        //Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("admin");

        doThrow(UserAlreadyExistsException.class).when(userService).register(any(UserRegisterDTO.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/saveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().isOk()).andExpect(view().name("/user/admin/saveUser"));
        verify(userService,times(1)).register(any(UserRegisterDTO.class));
    }

    @Test
    void updateUserAdminMainGetTest() throws Exception {
        //Arrange
        int id = 1;

        //Act
        ResultActions resp = mockMvc.perform(get("/user/admin/updateUser/{id}",id));

        //Assert
        resp.andExpect(status().isOk()).andExpect(view().name("/user/admin/updateUser")).andExpect(model().attributeExists( "UserRegisterDTO" ));
    }

    @Test
    void updateUserAdminMainPostTest() throws Exception {
        //Arrange
        int id = 1;
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("admin");

        doNothing().when(userService).update(any(UserRegisterDTO.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/updateUser")
                .sessionAttr("UserID",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(redirectedUrl("/user/admin/userAdminMain"));
        verify(userService,times(1)).update(any(UserRegisterDTO.class),any(Integer.class));
    }

    @Test
    void updateUserAdminMainPostWhenUserNotFoundTest() throws Exception {
        //Arrange
        int id = 1;
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("admin");

        doThrow(UserNotFoundException.class).when(userService).update(any(UserRegisterDTO.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/updateUser")
                .sessionAttr("UserID",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(redirectedUrl("/user/admin/userAdminMain"));
        verify(userService, times(1)).update(any(UserRegisterDTO.class),any(Integer.class));
    }

    @Test
    void deleteUserGetTest() throws Exception {
        //Arrange
        String id = "1";
        int expectedStatus = 302;

        doNothing().when(userService).delete(any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/admin/deleteUser/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/admin/userAdminMain"));
        verify(userService,times(1)).delete(any(String.class));
    }

    @Test
    void deleteUserGetWhenUserNotFoundTest() throws Exception {
        //Arrange
        String id = "1";
        int expectedStatus = 302;

        doThrow(UserNotFoundException.class).when(userService).delete(any(String.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/admin/deleteUser/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/admin/userAdminMain"));
        verify(userService,times(1)).delete(any(String.class));
    }

    @Test
    void deleteUserPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        String id = "1";

        //Act
        ResultActions resp = mockMvc.perform(post("/user/admin/deleteUser/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void viewUserViewGetTest() throws Exception {
        //Arrange
        ArrayList<BookDTO> books = new ArrayList<>();
        int id = 1;

        when(bookService.getAllBooksByUserId(any(Integer.class))).thenReturn(books);


        //Act
        ResultActions resp = mockMvc.perform(get("/user/user/userMain").sessionAttr("ActualUser",id));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(model().attribute("ActualUser",id))
                .andExpect(model().attribute("books",books))
                .andExpect(view().name("/user/user/userMain"));
        verify(bookService,times(1)).getAllBooksByUserId(any(Integer.class));
    }

    @Test
    void viewUserViewPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/userMain"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void viewLibraryViewGetTest() throws Exception {
        //Arrange
        ArrayList<LibraryDTO> libraries = new ArrayList<>();

        when(libraryService.findAll()).thenReturn(libraries);


        //Act
        ResultActions resp = mockMvc.perform(get("/user/user/userLibraries"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(model().attribute("libraries",libraries))
                .andExpect(view().name("/user/user/userLibraries"));
        verify(libraryService,times(1)).findAll();
    }

    @Test
    void viewLibraryViewPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/userLibraries"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void updateUserMainGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/user/user/updateUser"));
        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/user/user/updateUser"))
                .andExpect(model().attributeExists("UserRegisterDTO"));
    }

    @Test
    void updateUserMainPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        doNothing().when(userService).update(any(UserRegisterDTO.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/updateUser")
                .sessionAttr("ActualUser",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userMain"));
        verify(userService,times(1)).update(any(UserRegisterDTO.class),any(Integer.class));
    }

    @Test
    void updateUserMainPostWhenUserNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        int id = 1;
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        doThrow(UserNotFoundException.class).when(userService).update(any(UserRegisterDTO.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/updateUser")
                .sessionAttr("ActualUser",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userMain"));
        verify(userService,times(1)).update(any(UserRegisterDTO.class),any(Integer.class));
    }

    @Test
    void borrowBookGetTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doNothing().when(libraryService).borrowBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/borrow/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userBooksLibrary/"+libraryId));
        verify(libraryService,times(1)).borrowBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void borrowBookGetWhenLogNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(LogNotFoundException.class).when(libraryService).borrowBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/borrow/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userBooksLibrary/"+libraryId));
        verify(libraryService,times(1)).borrowBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void borrowBookGetWhenUserNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(UserNotFoundException.class).when(libraryService).borrowBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/borrow/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userBooksLibrary/"+libraryId));
        verify(libraryService,times(1)).borrowBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void borrowBookGetWhenBookIsBookedTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(BookIsBookedException.class).when(libraryService).borrowBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/borrow/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userBooksLibrary/"+libraryId));
        verify(libraryService,times(1)).borrowBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void borrowBookGetUserAlreadyBookedThisBookWhenTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(UserAlreadyBookedThisBookException.class).when(libraryService).borrowBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/borrow/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));

        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userBooksLibrary/"+libraryId));
        verify(libraryService,times(1)).borrowBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void borrowBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        String id = "1";

        //Act
        ResultActions resp = mockMvc.perform(post("/user/borrow/book/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void returnBookGetTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doNothing().when(libraryService).returnBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/return/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));
        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userMain"));
        verify(libraryService,times(1)).returnBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void returnBookGetWhenLogNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(LogNotFoundException.class).when(libraryService).returnBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/return/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));
        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userMain"));
        verify(libraryService,times(1)).returnBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void returnBookGetWhenBorrowNotFoundTest() throws Exception {
        //Arrange
        int expectedStatus = 302;
        String id = "1";
        int userId = 1;
        int libraryId = 1;

        doThrow(BorrowNotFoundException.class).when(libraryService).returnBook(any(Integer.class),any(String.class),any(Integer.class));

        //Act
        ResultActions resp = mockMvc.perform(get("/user/return/book/{id}",id)
                .sessionAttr("ActualUser",userId)
                .sessionAttr("LibraryID",libraryId));
        //Assert
        resp.andExpect(status().is(expectedStatus)).andExpect(redirectedUrl("/user/user/userMain"));
        verify(libraryService,times(1)).returnBook(any(Integer.class),any(String.class),any(Integer.class));
    }

    @Test
    void returnBookPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        String id = "1";

        //Act
        ResultActions resp = mockMvc.perform(post("/user/return/book/{id}",id));

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
        ResultActions resp = mockMvc.perform(get("/user/user/userBooksLibrary/{id}",id));

        //Assert
        resp.andExpect(status().isOk()).andExpect(view().name("/user/user/userBooksLibrary")).andExpect(model().attribute("books", books));
        verify(logService,times(1)).getAllBooksAvailableByLibraryId(any(Integer.class));
    }

    @Test
    void enterLibraryMainPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        int id = 1;

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/userBooksLibrary/{id}",id));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void userLogGetTest() throws Exception {
        //Arrange
        int id = 1;
        ArrayList<LogDTO> logs = new ArrayList<>();

        when(logService.getAllLogsByUserId(any(Integer.class))).thenReturn(logs);

        //Act
        ResultActions resp = mockMvc.perform(get("/user/user/userLog").sessionAttr("ActualUser",id));

        //Assert
        resp.andExpect(status().isOk()).andExpect(view().name("/user/user/userLog")).andExpect(model().attribute("logs", logs));
        verify(logService,times(1)).getAllLogsByUserId(any(Integer.class));
    }

    @Test
    void userLogPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(post("/user/user/userLog"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }
}
