package com.roma.apitelegramstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice // вывод текста во время ошибки для пользователя
public class GlobalExceptionHandler extends RuntimeException {

    // продукт не найден
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // недостаточно товара, количества
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleBadRequest(NotEnoughStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // 1. Берем список всех ошибок полей из эксепшена: ex.getBindingResult().getFieldErrors()
        // 2. Открываем стрим и собираем их в Мапу формата: "Имя поля" -> "Текст твоей ошибки"
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField, // Ключ мапы — имя косячного поля (например, "price")
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Ошибка валидации" // Значение — твой текст
                ));

        // 3. Возвращаем эту мапу со статусом 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}




