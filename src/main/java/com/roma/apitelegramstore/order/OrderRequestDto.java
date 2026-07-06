package com.roma.apitelegramstore.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank(message = "Имя покупателя не может быть пустым!")
    private String customerName;


    // Проверяем, что корзина не пустая, и валидируем каждый элемент внутри списка!
    @NotEmpty(message = "Корзина покупок не может быть пустой!")
    @Valid
    private List<OrderItemDto> items;
}
