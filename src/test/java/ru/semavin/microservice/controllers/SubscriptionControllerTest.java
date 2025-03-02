package ru.semavin.microservice.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.semavin.microservice.config.TestConfig;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.services.SubscriptionService;
import ru.semavin.microservice.util.ExceptionFactory;
import ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException;
import ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для {@link SubscriptionController}.
 * <p>
 * Покрывает основные сценарии использования API:
 * <ul>
 *     <li>Оформление подписки</li>
 *     <li>Получение списка подписок</li>
 *     <li>Удаление подписки</li>
 *     <li>Ошибки: пользователь не найден, подписка не найдена, подписка не принадлежит пользователю</li>
 *     <li>Ошибки уникальности (пользователь уже подписан на сервис)</li>
 * </ul>
 * </p>
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SubscriptionController.class)
@Import({GlobalAdviceController.class, TestConfig.class})
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionService subscriptionService;

    private SubscriptionDTO validSubscription;

    @BeforeEach
    void setUp() {
        validSubscription = SubscriptionDTO.builder()
                .id(10L)
                .serviceName("Test Service")
                .build();
    }

    /**
     * Сценарий успешного оформления подписки.
     * Ожидаем 200 OK и возвращение данных оформленной подписки.
     */
    @Test
    @DisplayName("subscribe_Success: Успешно оформляет подписку")
    void subscribe_Success() throws Exception {
        Mockito.when(subscriptionService.subscribe(anyLong(), any(SubscriptionDTO.class)))
                .thenReturn(validSubscription);

        mockMvc.perform(post("/api/v1/users/1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceName\":\"Test Service\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.serviceName").value("Test Service"));
    }

    /**
     * Сценарий, когда пользователь не найден при оформлении подписки.
     * Ожидаем 404 Not Found.
     */
    @Test
    @DisplayName("subscribe_UserNotFound: Возвращает 404, если пользователь не найден")
    void subscribe_UserNotFound() throws Exception {
        Mockito.when(subscriptionService.subscribe(anyLong(), any(SubscriptionDTO.class)))
                .thenThrow(ExceptionFactory.userNotFound(999L));

        mockMvc.perform(post("/api/v1/users/999/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceName\":\"Test Service\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с id: 999 не найден"));
    }

    /**
     * Сценарий, когда пользователь уже подписан на этот сервис.
     * Ожидаем 409 Conflict.
     */
    @Test
    @DisplayName("subscribe_AlreadyExists: Возвращает 409, если пользователь уже подписан на сервис")
    void subscribe_AlreadyExists() throws Exception {
        Mockito.when(subscriptionService.subscribe(anyLong(), any(SubscriptionDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Пользователь уже подписан на этот сервис"));

        mockMvc.perform(post("/api/v1/users/1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceName\":\"Test Service\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Пользователь уже подписан на этот сервис"));
    }

    /**
     * Сценарий успешного получения списка подписок.
     * Ожидаем 200 OK и список подписок.
     */
    @Test
    @DisplayName("getSubscriptions_Success: Успешно получает список подписок")
    void getSubscriptions_Success() throws Exception {
        Mockito.when(subscriptionService.getSubscriptions(anyLong()))
                .thenReturn(Collections.singletonList(validSubscription));

        mockMvc.perform(get("/api/v1/users/1/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].serviceName").value("Test Service"));
    }

    /**
     * Сценарий, когда пользователь не найден при получении списка подписок.
     * Ожидаем 404 Not Found.
     */
    @Test
    @DisplayName("getSubscriptions_UserNotFound: Возвращает 404, если пользователь не найден")
    void getSubscriptions_UserNotFound() throws Exception {
        Mockito.when(subscriptionService.getSubscriptions(anyLong()))
                .thenThrow(ExceptionFactory.userNotFound(5L));

        mockMvc.perform(get("/api/v1/users/5/subscriptions"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с id: 5 не найден"));
    }

    /**
     * Сценарий успешного удаления подписки.
     * Ожидаем 204 No Content.
     */
    @Test
    @DisplayName("unsubscribe_Success: Успешно удаляет подписку")
    void unsubscribe_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1/subscriptions/10"))
                .andExpect(status().isNoContent());
    }

    /**
     * Сценарий, когда подписка не найдена.
     * Ожидаем 404 Not Found.
     */
    @Test
    @DisplayName("unsubscribe_SubscriptionNotFound: Возвращает 404, если подписка не найдена")
    void unsubscribe_SubscriptionNotFound() throws Exception {
        Mockito.doThrow(new SubscriptionNotFoundException("Подписка с id: 15 не найдена"))
                .when(subscriptionService).unsubscribe(1L, 15L);

        mockMvc.perform(delete("/api/v1/users/1/subscriptions/15"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Подписка с id: 15 не найдена"));
    }

    /**
     * Сценарий, когда подписка не принадлежит пользователю.
     * Ожидаем 403 Forbidden.
     */
    @Test
    @DisplayName("unsubscribe_SubscriptionNotBelongToUser: Возвращает 403, если подписка не принадлежит пользователю")
    void unsubscribe_SubscriptionNotBelongToUser() throws Exception {
        Mockito.doThrow(new SubscriptionNotBelongToUserException("Подписка 20 не принадлежит пользователю 1"))
                .when(subscriptionService).unsubscribe(1L, 20L);

        mockMvc.perform(delete("/api/v1/users/1/subscriptions/20"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Подписка 20 не принадлежит пользователю 1"));
    }
}
