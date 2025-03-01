package ru.semavin.microservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.semavin.microservice.dtos.ErrorResponseDTO;
import ru.semavin.microservice.util.exceptions.UserNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalAdviceController {

    /**
     * Обрабатывает исключение UserNotFoundException.
     *
     * @param ex выброшенное исключение
     * @return JSON-ответ с деталями ошибки
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
}
