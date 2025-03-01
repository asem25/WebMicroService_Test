package ru.semavin.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.services.SubscriptionService;

import java.util.List;

/**
 * Контроллер для управления подписками пользователей.
 *
 * <p>Обеспечивает API для подписки, получения списка подписок и отписки от сервисов.</p>
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/subscriptions")
@Tag(name = "Subscription API", description = "API для подписок")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Оформляет подписку для пользователя.
     *
     * @param userId ID пользователя, который оформляет подписку.
     * @param subscriptionDTO DTO с данными подписки.
     * @return DTO созданной подписки.
     */
    @Operation(summary = "Оформить подписку пользователю",
            description = "Создаёт новую подписку для указанного пользователя.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Подписка успешно оформлена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping
    public ResponseEntity<SubscriptionDTO> subscribe(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("userId") Long userId,
            @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.ok(subscriptionService.subscribe(userId, subscriptionDTO));
    }

    /**
     * Получает список подписок пользователя.
     *
     * @param userId ID пользователя.
     * @return Список подписок пользователя.
     */
    @Operation(summary = "Получить список подписок пользователя",
            description = "Возвращает список подписок, оформленных пользователем.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список подписок пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userId));
    }

    /**
     * Отменяет подписку пользователя.
     *
     * @param userId ID пользователя.
     * @param subId  ID подписки.
     * @return HTTP 204 (No Content), если подписка успешно удалена.
     */
    @Operation(summary = "Отменить подписку пользователя",
            description = "Удаляет подписку, если она принадлежит пользователю.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Подписка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Подписка или пользователь не найдены"),
            @ApiResponse(responseCode = "403", description = "Подписка не принадлежит пользователю")
    })
    @DeleteMapping("/{sub_id}")
    public ResponseEntity<Void> unsubscribe(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("userId") Long userId,
            @Parameter(description = "ID подписки", required = true, example = "10")
            @PathVariable("sub_id") Long subId) {
        subscriptionService.unsubscribe(userId, subId);
        return ResponseEntity.noContent().build();
    }
}
