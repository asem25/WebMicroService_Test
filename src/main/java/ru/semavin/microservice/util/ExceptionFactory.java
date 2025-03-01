package ru.semavin.microservice.util;

import ru.semavin.microservice.util.exceptions.UserNotFoundException;

public final class ExceptionFactory {
    private ExceptionFactory() {}

    public static UserNotFoundException userNotFound(Long userId) {
        return new UserNotFoundException(String.format("Пользователь с id: %d не найден", userId));
    }
}
