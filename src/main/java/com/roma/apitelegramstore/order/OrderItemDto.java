package com.roma.apitelegramstore.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    @NotNull(message = "ID товара должен быть указан!")
    private Long productId;

    @NotNull(message = "Количество должно быть указано!")
    @Min(value = 1, message = "Минимальное количество — 1")
    private Integer quantity;
}
