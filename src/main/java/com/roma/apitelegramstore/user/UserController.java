package com.roma.apitelegramstore.user;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // Внедряем менеджер Спринга
    private final JwtCore jwtCore; // Внедряем наш генератор токенов

    // Обновленный конструктор со всеми зависимостями
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }


    // Твой старый метод регистрации остается на месте
    @PostMapping("/register")
    public User register(@Valid @RequestBody UserRegisterDto dto) {
        return userService.registerNewUser(dto);
    }


    // НОВЫЙ МЕТОД: Вход в систему
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDto dto) {

        // 1. Просим Спринг проверить логин и пароль
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        // 2. Если проверка прошла успешно — выпекаем и возвращаем токен в виде строки!
        return jwtCore.generateToken(authentication);
    }
}
