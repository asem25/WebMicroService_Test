package ru.semavin.microservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.semavin.microservice.dtos.ErrorResponseDTO;
import ru.semavin.microservice.util.exceptions.SubscriptionNotBelongToUserException;
import ru.semavin.microservice.util.exceptions.SubscriptionNotFoundException;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;

import java.util.List;

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
    /**
     * Обрабатывает ошибки валидации (`@Valid`).
     *
     * @param ex исключение, содержащее информацию о полях с ошибками.
     * @return JSON-ответ с деталями ошибки.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Ошибка валидации: {}", ex.getMessage());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("Поле '%s': %s", error.getField(), error.getDefaultMessage()))
                .toList();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .build());
    }
    /**
     * Обрабатывает ошибки нарушения целостности данных в базе
     *
     * <p>Этот обработчик вызывается, когда в базе данных возникает ошибка типа
     * {@link org.springframework.dao.DataIntegrityViolationException}, например,
     * при попытке создать дублирующуюся запись подписки (нарушение ограничения `UNIQUE`).</p>
     *
     * <p>Если пользователь уже подписан на сервис, метод возвращает HTTP статус {@code 409 Conflict}.</p>
     *
     *
     * @param ex выброшенное исключение {@link org.springframework.dao.DataIntegrityViolationException}.
     * @return JSON-ответ {@link ErrorResponseDTO} с HTTP статусом {@code 409 Conflict} и сообщением об ошибке.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Ошибка уникальности данных: {}", ex.getMessage());

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Пользователь уже подписан на этот сервис")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
