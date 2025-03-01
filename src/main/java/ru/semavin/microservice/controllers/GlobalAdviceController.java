package ru.semavin.microservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.semavin.microservice.dtos.ErrorResponseDTO;
import ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException;
import ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;

/**
 * Глобальный обработчик исключений для REST API.
 * <p>
 * Этот класс перехватывает исключения, возникающие в контроллерах, и
 * возвращает клиенту понятные JSON-ответы с описанием ошибки.
 * </p>
 */
@ControllerAdvice
@Slf4j
public class GlobalAdviceController {

    /**
     * Обрабатывает исключение {@link UserNotFoundException}.
     * <p>
     * Возвращает HTTP статус 404 (Not Found) с сообщением о том, что пользователь не найден.
     * </p>
     *
     * @param ex выброшенное исключение {@link UserNotFoundException}.
     * @return JSON-ответ {@link ErrorResponseDTO} с описанием ошибки.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Ошибка: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build()
        );
    }

    /**
     * Обрабатывает исключение {@link SubscriptionNotFoundException}.
     * <p>
     * Возвращает HTTP статус 404 (Not Found), если подписка не найдена.
     * </p>
     *
     * @param ex выброшенное исключение {@link SubscriptionNotFoundException}.
     * @return JSON-ответ {@link ErrorResponseDTO} с деталями ошибки.
     */
    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex) {
        log.error("Ошибка: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build()
        );
    }

    /**
     * Обрабатывает исключение {@link SubscriptionNotBelongToUserException}.
     * <p>
     * Возвращает HTTP статус 403 (Forbidden), если подписка не принадлежит указанному пользователю.
     * </p>
     *
     * @param ex выброшенное исключение {@link SubscriptionNotBelongToUserException}.
     * @return JSON-ответ {@link ErrorResponseDTO} с информацией об ошибке.
     */
    @ExceptionHandler(SubscriptionNotBelongToUserException.class)
    public ResponseEntity<ErrorResponseDTO> handleSubscriptionNotBelongToUserException(SubscriptionNotBelongToUserException ex) {
        log.error("Ошибка: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(ex.getMessage())
                        .build()
        );
    }
}
