package ru.semavin.microservice.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для {@link UserController}.
 * <p>
 * Покрывает все основные сценарии, включая:
 * <ul>
 *     <li>Успешное создание пользователя</li>
 *     <li>Валидация входных данных (BadRequest)</li>
 *     <li>Обработка ситуаций, когда пользователь не найден</li>
 *     <li>Обновление и удаление пользователя</li>
 * </ul>
 * </p>
 */
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

    /**
     * Проверяем сценарий, когда пользователь не найден по ID.
     * Ожидаем 404 Not Found и сообщение об ошибке.
     */
    @Test
    @DisplayName("getUser_NotFound: Возвращает 404, если пользователь не найден")
    void getUser_NotFound() throws Exception {
        Mockito.when(userService.findUserDTOById(Mockito.anyLong()))
                .thenThrow(ExceptionFactory.userNotFound(1L)); // Явно указываем ID

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Пользователь с id: 1 не найден"));
    }

    /**
     * Проверяем, что при некорректных данных (пустое имя, неверный email) запрос
     * обрабатывается на уровне валидации (MethodArgumentNotValidException) и возвращает 400.
     */
    @Test
    @DisplayName("createUser_BadRequest: Возвращает 400, если данные не проходят валидацию")
    void createUser_BadRequest() throws Exception {
        String invalidUserJson = "{\"name\":\"\", \"email\":\"invalid-email\"}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Имя пользователя не может быть пустым")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Некорректный формат email")));
    }

    /**
     * Проверяем сценарий, когда пользователь успешно создаётся.
     * Ожидаем 201 Created и заполненный UserDTO в ответе.
     */
    @Test
    @DisplayName("createUser_Success: Успешно создаёт пользователя и возвращает 201")
    void createUser_Success() throws Exception {
       Mockito.when(userService.createUser(any(UserDTO.class))).thenReturn(testUser);

        String validUserJson = "{\"name\":\"New User\", \"email\":\"new.user@example.com\"}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    /**
     * Проверяем сценарий, когда пытаемся обновить несуществующего пользователя.
     * Ожидаем 404 Not Found.
     */
    @Test
    @DisplayName("updateUser_NotFound: Возвращает 404, если пользователь не найден")
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

    /**
     * Проверяем, что при неверных данных при обновлении пользовательского профиля возвращается 400.
     */
    @Test
    @DisplayName("updateUser_BadRequest: Возвращает 400, если данные не проходят валидацию")
    void updateUser_BadRequest() throws Exception {
        String invalidUserJson = "{\"name\":\"\", \"email\":\"invalid-email\"}";

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Имя пользователя не может быть пустым")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("Некорректный формат email")));
    }

    /**
     * Проверяем сценарий удаления несуществующего пользователя.
     * Ожидаем 404 Not Found.
     */
    @Test
    @DisplayName("deleteUser_NotFound: Возвращает 404, если пользователь не найден")
    void deleteUser_NotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("User not found"))
                .when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    /**
     * Проверяем сценарий успешного получения пользователя.
     * Ожидаем 200 OK и правильные данные в JSON.
     */
    @Test
    @DisplayName("getUser_Success: Возвращает 200 и данные пользователя")
    void getUser_Success() throws Exception {
       Mockito.when(userService.findUserDTOById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    /**
     * Проверяем сценарий успешного получения списка всех пользователей.
     * Ожидаем 200 OK и массив.
     */
    @Test
    @DisplayName("getAllUsers_Success: Возвращает 200 и список пользователей")
    void getAllUsers_Success() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }
}
