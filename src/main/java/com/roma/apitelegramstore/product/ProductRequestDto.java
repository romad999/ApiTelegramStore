package com.roma.apitelegramstore.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

// импорты

@Getter
@Setter
public class ProductRequestDto {

    @NotBlank(message = "Название товара не может быть пустым!")
    private String title;

    @NotNull(message = "Цена должна быть указана!")
    @PositiveOrZero(message = "Цена не может быть отрицательной!")
    private Double price;

    @NotNull(message = "Количество должно быть указано!")
    @PositiveOrZero(message = "Количество не может быть меньше нуля!")
    private Integer quantity;
}
