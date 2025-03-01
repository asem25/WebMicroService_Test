package ru.semavin.microservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.dtos.SubscriptionTopDTO;
import ru.semavin.microservice.mapper.SubscriptionMapper;
import ru.semavin.microservice.models.Subscription;
import ru.semavin.microservice.models.User;
import ru.semavin.microservice.repositrories.SubscriptionRepository;
import ru.semavin.microservice.util.ExceptionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (subscriptionRepository.existsByUserAndServiceName(user, subscriptionDTO.getServiceName())) {
            throw new DataIntegrityViolationException("Пользователь уже подписан на этот сервис");
        }

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
    /**
     * Получает ТОП-3 самых популярных подписок по количеству пользователей.
     *
     * <p>Метод выполняет следующие шаги:</p>
     * <ol>
     *     <li>Запрашивает названия ТОП-3 подписок с наибольшим количеством пользователей.</li>
     *     <li>Загружает все подписки, относящиеся к этим названиям.</li>
     *     <li>Группирует подписки по названию сервиса и считает количество подписчиков.</li>
     *     <li>Преобразует данные в DTO и сортирует по убыванию популярности.</li>
     * </ol>
     *
     * @return Список {@link SubscriptionTopDTO}, содержащий ТОП-3 подписок.
     */
    public List<SubscriptionTopDTO> getTopSubscriptions() {
        List<String> subscriptionsNames = subscriptionRepository.findAllTop().stream()
                .limit(3)
                .toList();

        List<Subscription> subscriptionList = subscriptionRepository.findByServiceNameIn(subscriptionsNames);

        Map<String, Long> subscriptionCountMap = subscriptionList.stream()
                .collect(Collectors.groupingBy(Subscription::getServiceName, Collectors.counting()));

        return subscriptionCountMap.entrySet().stream()
                .map(entry -> SubscriptionTopDTO.builder()
                        .serviceName(entry.getKey())
                        .count(entry.getValue())
                        .build()
                ).sorted(Comparator.comparingLong(SubscriptionTopDTO::getCount).reversed())
                .toList();
    }
}
