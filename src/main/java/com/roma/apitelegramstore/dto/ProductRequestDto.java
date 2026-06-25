package com.roma.apitelegramstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String title;
    private Double price;
    private Integer quantity;
}
