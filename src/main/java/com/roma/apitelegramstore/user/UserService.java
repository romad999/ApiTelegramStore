package com.roma.apitelegramstore.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(UserRegisterDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует!");
            // Позже заменим на кастомный эксепшен, пока для скорости оставим так
        }

        User user = new User();
        user.setUsername(dto.getUsername());

        // ХЕШИРУЕМ ПАРОЛЬ! В базу улетит зашифрованная каша, а не сырой текст
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);

        // Каждому новому зарегистрированному даем роль обычного пользователя
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }

}
