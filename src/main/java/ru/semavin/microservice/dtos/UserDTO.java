package ru.semavin.microservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO пользователя.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "UserDTO", description = "Модель данных пользователя")
public class UserDTO {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    /**
     * Имя пользователя.
     */
    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;

    /**
     * Email пользователя.
     */
    @Schema(description = "Email пользователя", example = "ivan@example.com")
    private String email;
}