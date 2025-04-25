package org.biblioteca.bibliotecamvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biblioteca.bibliotecamvc.business.dto.UserDTO;
import org.biblioteca.bibliotecamvc.business.dto.UserRegisterDTO;
import org.biblioteca.bibliotecamvc.business.exception.user.PasswordNotMatchException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserIsDeletedException;
import org.biblioteca.bibliotecamvc.business.exception.user.UserNotFoundException;
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

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = IndexController.class)
public class IndexControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void indexGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/main"));

        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void indexPostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        //Act
        ResultActions resp = mockMvc.perform(post("/main"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void homeGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/"));
        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/auth/login"))
                .andExpect(model().attributeExists("UserRegisterDTO"));
    }

    @Test
    void homePostTest() throws Exception {
        //Arrange
        int expectedStatus = 405;
        //Act
        ResultActions resp = mockMvc.perform(post("/"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void registerGetTest() throws Exception {
        //Arrange
        //Act
        ResultActions resp = mockMvc.perform(get("/form-register"));
        //Assert
        resp.andExpect(status().isOk())
                .andExpect(view().name("/auth/register"))
                .andExpect(model().attributeExists("UserRegisterDTO"));
    }

    @Test
    void registerPostTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        doNothing().when(userService).register(any(UserRegisterDTO.class));

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/"));

        verify(userService,times(1)).register(any(UserRegisterDTO.class));
    }

    @Test
    void loginGetTest() throws Exception {
        //Arrange
        int expectedStatus = 405;

        //Act
        ResultActions resp = mockMvc.perform(get("/auth/login"));

        //Assert
        resp.andExpect(status().is(expectedStatus));
    }

    @Test
    void loginPostWhenAdminTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setAdmin(true);

       when(userService.login(any(UserRegisterDTO.class))).thenReturn(userDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/main"));

        verify(userService,times(1)).login(any(UserRegisterDTO.class));
    }

    @Test
    void loginPostWhenNoAdminTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setAdmin(false);

        when(userService.login(any(UserRegisterDTO.class))).thenReturn(userDTO);

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/user/user/userMain"));

        verify(userService,times(1)).login(any(UserRegisterDTO.class));
    }

    @Test
    void loginPostWhenPasswordNotMatchExceptionTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        when(userService.login(any(UserRegisterDTO.class))).thenThrow(PasswordNotMatchException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/"));

        verify(userService,times(1)).login(any(UserRegisterDTO.class));
    }

    @Test
    void loginPostWhenUserNotFoundExceptionTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        when(userService.login(any(UserRegisterDTO.class))).thenThrow(UserNotFoundException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/"));

        verify(userService,times(1)).login(any(UserRegisterDTO.class));
    }

    @Test
    void loginPostWhenUserIsDeletedExceptionTest() throws Exception {
        //Arrange
        int expectedStatus = 302;

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setPassword("password");

        when(userService.login(any(UserRegisterDTO.class))).thenThrow(UserIsDeletedException.class);

        //Act
        ResultActions resp = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(userRegisterDTO)));

        //Assert
        resp.andExpect(status().is(expectedStatus))
                .andExpect(view().name("redirect:/"));

        verify(userService,times(1)).login(any(UserRegisterDTO.class));
    }
}
