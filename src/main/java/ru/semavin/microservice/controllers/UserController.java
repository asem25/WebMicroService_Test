package ru.semavin.microservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.microservice.dtos.UserDTO;
import ru.semavin.microservice.services.UserService;

import java.net.URI;
import java.util.List;

/**
 * Контроллер для управления пользователями.
 * <p>Обеспечивает CRUD-операции для управления пользователями.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
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
    @Operation(summary = "Создать пользователя",
            description = "Создаёт нового пользователя в системе.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные пользователя")
            @RequestBody UserDTO userDTO) {
        log.info("Endpoint -'POST /users': Создание пользователя: {}", userDTO);
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
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по указанному идентификатору.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Endpoint -'GET /users/{id}': Получение пользователя с id: {}", id);
        UserDTO userDto = userService.findUserDTOById(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Обновление данных пользователя.
     *
     * @param id      Идентификатор пользователя.
     * @param userDto Новые данные пользователя.
     * @return Обновлённый пользователь.
     */
    @Operation(summary = "Обновить пользователя по ID",
            description = "Обновляет данные пользователя по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Новые данные пользователя")
            @RequestBody @Valid UserDTO userDto) {
        log.info("Endpoint -'PUT /users/{id}': Обновление пользователя с id: {}. Новые данные: {}", id, userDto);
        UserDTO updated = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удаление пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Статус 204 (No Content) при успешном удалении.
     */
    @Operation(summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по указанному идентификатору.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Endpoint -'DELETE /users/{id}': Удаление пользователя с id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение списка всех пользователей.
     *
     * @return Список всех пользователей.
     */
    @Operation(summary = "Получить список всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Endpoint -'GET /users': Получение списка всех пользователей");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
