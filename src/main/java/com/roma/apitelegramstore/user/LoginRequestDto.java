package com.roma.apitelegramstore.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Логин не может быть пустым!")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым!")
    private String password;
}
