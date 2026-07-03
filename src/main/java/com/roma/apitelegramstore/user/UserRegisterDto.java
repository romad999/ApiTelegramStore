package com.roma.apitelegramstore.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    @NotBlank(message = "Логин не может быть пустым!")
    @Size(min = 4, max = 20, message = "Логин должен быть от 4 до 20 символов!")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым!")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов!")
    private String password;
}
