package ru.semavin.microservice.util;

import lombok.extern.slf4j.Slf4j;
import ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException;
import ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;
@Slf4j
public final class ExceptionFactory {
    private ExceptionFactory() {}

    public static UserNotFoundException userNotFound(Long userId) {
        log.warn("Пользователь с ID {} не найден", userId);
        return new UserNotFoundException(String.format("Пользователь с id: %d не найден", userId));
    }
    public static SubscriptionNotFoundException subscriptionNotFound(Long subscriptionId) {
        return new SubscriptionNotFoundException(String.format("Подписка с id: %d не найдена", subscriptionId));
    }
    public static SubscriptionNotBelongToUserException subscriptionNotBelongToUser(Long userId, Long subscriptionId) {
        return new SubscriptionNotBelongToUserException(String
                .format("Подписка %d не принадлежит пользователю %d", subscriptionId, userId));
    }
}
