package ru.semavin.microservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "UserDTO", description = "Модель данных пользователя")
public class UserDTO {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    /**
     * Имя пользователя.
     */
    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 2 до 50 символов")
    private String name;

    /**
     * Email пользователя.
     */
    @Schema(description = "Email пользователя", example = "ivan@example.com")
    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
}
