package com.roma.apitelegramstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // вывод текста во время ошибки для пользователя
public class GlobalExceptionHandler extends RuntimeException {

    // продукт не найден
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    // недостаточно товара, количества
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleBadRequest(NotEnoughStockException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
