package ru.semavin.microservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.microservice.dtos.UserDTO;
import ru.semavin.microservice.services.UserService;

import java.net.URI;
import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Предоставляет CRUD-операции для сущности User.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Операции для управления пользователями")
public class UserController {

    private final UserService userService;

    /**
     * Создание нового пользователя.
     *
     * @param userDTO Объект, содержащий данные пользователя.
     * @return Созданный пользователь с присвоенным ID.
     */
    @Operation(summary = "Создать пользователя")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        log.info("Endpoint -'post /users': Создание пользователя: {}", userDTO);
        UserDTO created = userService.createUser(userDTO);
        return ResponseEntity
                .created(URI.create("/users/" + created.getId()))
                .body(created);
    }

    /**
     * Получение информации о пользователе по его ID.
     *
     * @param id Идентификатор пользователя.
     * @return DTO пользователя, если найден.
     */
    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("Endpoint -'get /users/{id}': Получение пользователя с id: {}", id);
        UserDTO userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Обновление данных пользователя.
     *
     * @param id      Идентификатор пользователя.
     * @param userDto Новые данные пользователя.
     * @return Обновлённый пользователь.
     */
    @Operation(summary = "Обновить пользователя по ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto) {
        log.info("Endpoint -'put /users/{id}': Обновление пользователя с id: {}. Новые данные: {}", id, userDto);
        UserDTO updated = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удаление пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Статус 204 (No Content) при успешном удалении.
     */
    @Operation(summary = "Удалить пользователя по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Endpoint -'delete /users/{id}': Удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение списка всех пользователей.
     * Этот метод может использоваться для отладки или обзора списка пользователей.
     *
     * @return Список всех пользователей.
     */
    @Operation(summary = "Получить список всех пользователей")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Endpoint -'get /users': Получение списка всех пользователей");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}

