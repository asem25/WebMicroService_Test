package ru.semavin.microservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.mapper.SubscriptionMapper;
import ru.semavin.microservice.models.Subscription;
import ru.semavin.microservice.models.User;
import ru.semavin.microservice.repositrories.SubscriptionRepository;
import ru.semavin.microservice.util.ExceptionFactory;

import java.util.List;
/**
 * Сервисный слой для управления подписками
 * Отвечает за создание, получение списка подписок, отмену подписки у пользователя.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;

    /**
     * Добавляет подписку пользователю.
     *
     * @param userId ID пользователя
     * @param subscriptionDTO Данные подписки
     * @return DTO созданной подписки
     */
    public SubscriptionDTO subscribe(Long userId, SubscriptionDTO subscriptionDTO) {
        log.info("Добавление подписки пользователю с ID: {}", userId);

        User user = userService.findUserById(userId);
        Subscription subscription = subscriptionMapper.toSubscription(subscriptionDTO);

        subscription.setUser(user);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        log.info("Подписка ID {} успешно создана для пользователя ID {}", savedSubscription.getId(), userId);
        return subscriptionMapper.toSubscriptionDTO(savedSubscription);
    }

    /**
     * Получает список подписок пользователя.
     *
     * @param userId ID пользователя
     * @return список подписок в формате DTO
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> getSubscriptions(Long userId) {
        log.info("Получение подписок для пользователя ID: {}", userId);

        User user = userService.findUserById(userId);
        return subscriptionRepository.findByUser(user).stream()
                .map(subscriptionMapper::toSubscriptionDTO)
                .toList();
    }

    /**
     * Удаляет подписку пользователя.
     *
     * <p>Метод проверяет существование подписки, а затем удостоверяется, что она принадлежит пользователю.
     * Если подписка не найдена, выбрасывается исключение {@link ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException}.
     * Если подписка найдена, но принадлежит другому пользователю, выбрасывается исключение
     * {@link ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException}.</p>
     *
     * @param userId ID пользователя, который хочет удалить подписку.
     * @param subId  ID подписки, которую необходимо удалить.
     * @throws ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException если подписка не найдена в системе.
     * @throws ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException если подписка существует, но принадлежит другому пользователю.
     */
    public void unsubscribe(Long userId, Long subId) {
        log.info("Попытка удаления подписки ID {} у пользователя ID {}", subId, userId);

        if (!subscriptionRepository.existsById(subId)) {
            log.warn("Подписки ID {} не существует", subId);
            throw ExceptionFactory.subscriptionNotFound(subId);
        }

        if (!subscriptionRepository.existsByIdAndUserId(subId, userId)) {
            log.warn("Подписка ID {} не принадлежит пользователю ID {}", subId, userId);
            throw ExceptionFactory.subscriptionNotBelongToUser(userId, subId);
        }

        subscriptionRepository.deleteById(subId);
        log.info("Подписка ID {} успешно удалена у пользователя ID {}", subId, userId);
    }
}
