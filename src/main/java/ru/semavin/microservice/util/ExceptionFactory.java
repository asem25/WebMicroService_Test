package ru.semavin.microservice.util;

import lombok.extern.slf4j.Slf4j;
import ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException;
import ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;

/**
 * Фабрика исключений, связанных с пользователями и подписками.
 * <p>
 * Предоставляет статические методы для создания конкретных типов исключений.
 * </p>
 */
@Slf4j
public final class ExceptionFactory {
    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляра.
     */
    private ExceptionFactory() {}

    /**
     * Создаёт исключение {@link UserNotFoundException}, если пользователь не найден.
     *
     * @param userId идентификатор пользователя, которого не удалось найти
     * @return экземпляр {@link UserNotFoundException} с подробным сообщением
     */
    public static UserNotFoundException userNotFound(Long userId) {
        log.warn("Пользователь с ID {} не найден", userId);
        return new UserNotFoundException(String.format("Пользователь с id: %d не найден", userId));
    }

    /**
     * Создаёт исключение {@link SubscriptionNotFoundException}, если подписка с указанным идентификатором не найдена.
     *
     * @param subscriptionId идентификатор подписки, которую не удалось найти
     * @return экземпляр {@link SubscriptionNotFoundException} с подробным сообщением
     */
    public static SubscriptionNotFoundException subscriptionNotFound(Long subscriptionId) {
        return new SubscriptionNotFoundException(String.format("Подписка с id: %d не найдена", subscriptionId));
    }
    /**
     * Создаёт исключение {@link SubscriptionNotBelongToUserException}, указывающее, что подписка не принадлежит пользователю.
     *
     * @param userId         идентификатор пользователя
     * @param subscriptionId идентификатор подписки, которая не принадлежит данному пользователю
     * @return экземпляр {@link SubscriptionNotBelongToUserException} с подробным сообщением
     */
    public static SubscriptionNotBelongToUserException subscriptionNotBelongToUser(Long userId, Long subscriptionId) {
        return new SubscriptionNotBelongToUserException(
                String.format("Подписка %d не принадлежит пользователю %d", subscriptionId, userId)
        );
    }
}
