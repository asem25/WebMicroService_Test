package ru.semavin.microservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для представления ошибок в JSON-ответе.
 * <p>Используется в {@code GlobalAdviceController} для возврата детализированных ошибок API.</p>
 */
@Data
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    /**
     * HTTP статус ошибки.
     */
    private int status;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Временная метка ошибки.
     * Формат: {@code yyyy-MM-dd HH:mm:ss}.
     */
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
}