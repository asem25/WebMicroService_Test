package ru.semavin.microservice.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.semavin.microservice.config.TestConfig;
import ru.semavin.microservice.dtos.UserDTO;
import ru.semavin.microservice.services.UserService;
import ru.semavin.microservice.util.ExceptionFactory;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
@Import({TestConfig.class, GlobalAdviceController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        testUser = UserDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void getUser_NotFound() throws Exception {
        Mockito.when(userService.findUserDTOById(Mockito.anyLong()))
                .thenThrow(ExceptionFactory.userNotFound(1L)); // Явно указываем ID

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))  // Проверяем статус в JSON-ответе
                .andExpect(jsonPath("$.message").value("Пользователь с id: 1 не найден")); // Проверяем сообщение
    }

    @Test
    void createUser_BadRequest() throws Exception {
        String invalidUserJson = "{\"name\":\"\", \"email\":\"invalid-email\"}";


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Имя пользователя не может быть пустым")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Некорректный формат email")));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(UserDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        String validUserJson = "{\"name\":\"Updated User\", \"email\":\"updated@example.com\"}";

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
    @Test
    void updateUser_BadRequest() throws Exception {
        String invalidUserJson = "{\"name\":\"\", \"email\":\"invalid-email\"}";


        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Имя пользователя не может быть пустым")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Некорректный формат email")));
    }
    @Test
    void deleteUser_NotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("User not found"))
                .when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
