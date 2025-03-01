package ru.semavin.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.dtos.SubscriptionTopDTO;
import ru.semavin.microservice.services.SubscriptionService;

import java.util.List;

/**
 * Глобальный контроллер для работы с подписками.
 * <p>Отвечает за операции, которые не привязаны к конкретному пользователю.</p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Global Subscription API", description = "Глобальные операции с подписками")
public class GlobalSubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Получает топ-3 самых популярных подписок.
     *
     * @return Список из трех самых популярных подписок.
     */
    @Operation(summary = "Получить топ-3 популярных подписок",
            description = "Возвращает список самых популярных подписок в системе.")
    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionTopDTO>> getTopSubscriptions() {
        log.info("Endpoint - 'GET /subscriptions/top': Получение ТОП-3 популярных подписок");
        List<SubscriptionTopDTO> topSubscriptions = subscriptionService.getTopSubscriptions();
        return ResponseEntity.ok(topSubscriptions);
    }
}
