package ru.semavin.microservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.microservice.dtos.UserDTO;
import ru.semavin.microservice.mapper.UserMapper;
import ru.semavin.microservice.models.User;
import ru.semavin.microservice.repositrories.UserRepository;
import ru.semavin.microservice.util.ExceptionFactory;

import java.util.List;

/**
 * Сервисный слой для управления пользователями.
 * Отвечает за создание, получение, обновление и удаление пользователей.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Создаёт нового пользователя.
     *
     * @param userDto DTO с данными нового пользователя.
     * @return DTO созданного пользователя.
     */
    public UserDTO createUser(UserDTO userDto) {
        log.info("Создание нового пользователя: {}", userDto);
        User user = userMapper.userDTOToUser(userDto);
        User createdUser = userRepository.save(user);
        log.info("Пользователь успешно создан: {}", createdUser);
        return userMapper.userToUserDTO(createdUser);
    }

    /**
     * Получает пользователя по ID.
     *
     * @param id идентификатор пользователя.
     * @return DTO пользователя, если он найден.
     * @throws ru.semavin.microservice.util.exceptions.UserNotFoundException если пользователь не найден.
     */
    @Transactional(readOnly = true)
    public UserDTO findUserDTOById(Long id) {
        log.info("Запрос информации о пользователе с ID: {}", id);

        return userMapper.userToUserDTO(findUserById(id));
    }

    /**
     * Обновляет данные существующего пользователя.
     * partial update.
     *
     * @param id      идентификатор пользователя.
     * @param userDto DTO с обновлёнными данными пользователя.
     * @return DTO обновлённого пользователя.
     * @throws ru.semavin.microservice.util.exceptions.UserNotFoundException если пользователь не найден.
     */
    public UserDTO updateUser(Long id, UserDTO userDto) {
        log.info("Обновление пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Попытка обновления несуществующего пользователя с ID {}", id);
                    return ExceptionFactory.userNotFound(id);
                });

        userMapper.updateUserFromDto(userDto, user);
        log.info("Данные пользователя с ID {} обновлены", id);
        return userMapper.userToUserDTO(user);
    }

    /**
     * Удаляет пользователя по ID.
     *
     * @param id идентификатор пользователя.
     * @throws ru.semavin.microservice.util.exceptions.UserNotFoundException если пользователь не найден.
     */
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("Попытка удаления несуществующего пользователя с ID {}", id);
            throw ExceptionFactory.userNotFound(id);
        }
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} успешно удалён", id);
    }

    /**
     * Получает список всех пользователей.
     *
     * @return список всех пользователей в виде DTO.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.info("Запрос списка всех пользователей");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .toList();
        log.info("Найдено {} пользователей", users.size());
        return users;
    }

    /**
     * Ищет пользователя по идентификатору.
     *
     * <p>Если пользователь найден, метод возвращает объект {@link User}.
     * Если пользователя нет в базе, выбрасывается исключение
     * {@link ru.semavin.microservice.util.exceptions.UserNotFoundException}.</p>
     *
     * @param id идентификатор пользователя, которого требуется найти.
     * @return объект {@link User}, если пользователь найден.
     * @throws ru.semavin.microservice.util.exceptions.UserNotFoundException если пользователь с указанным ID не найден.
     */
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.userNotFound(id));
    }
}
