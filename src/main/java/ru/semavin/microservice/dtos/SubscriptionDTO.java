package ru.semavin.microservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO подписки.
 */
@Data
@Schema(name = "SubscriptionDto", description = "Модель данных подписки пользователя")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    /**
     * Уникальный идентификатор подписки.
     */
    @Schema(description = "Уникальный идентификатор подписки")
    private Long id;

    /**
     * Идентификатор пользователя, которому принадлежит подписка.
     */
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;

    /**
     * Название сервиса, на который оформлена подписка.
     */
    @Schema(description = "Название сервиса", example = "Яндекс.Плюс")
    @NotBlank(message = "Название сервиса не может быть пустым")
    private String serviceName;

    /**
     * Флаг, указывающий, включены ли уведомления для подписки.
     */
    @Schema(description = "Включены ли уведомления", example = "true")
    @NotNull(message = "Поле 'notificationEnabled' обязательно")
    private boolean notificationEnabled;
}
