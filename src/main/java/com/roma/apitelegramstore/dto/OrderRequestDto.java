package com.roma.apitelegramstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    @NotNull(message = "ID товара должен быть указан!")
    private Long productId;

    @NotNull(message = "Количество должно быть указано!")
    @Min(value = 1, message = "Минимальное количество для заказа — 1 штука!")
    private Integer quantity;

    @NotBlank(message = "Имя покупателя не может быть пустым!")
    private String customerName;
}
