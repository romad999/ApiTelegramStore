package com.roma.apitelegramstore.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtCore {

    // 1. Генерируем супер-секретный ключ для подписи наших токенов.
    // Без этого ключа никто в интернете не сможет подделать твой токен!
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 2. Время жизни токена (пусть будет 24 часа в миллисекундах)
    private final long jwtExpirationMs = 86400000;

    // Метод генерации токена при успешном входе
    public String generateToken(Authentication authentication) {
        // Достаем нашего пользователя из объекта аутентификации Спринга
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Зашиваем логин в токен
                .claim("role", userPrincipal.getRole())   // Зашиваем его роль (например, ROLE_ADMIN)
                .setIssuedAt(new Date())                 // Время создания
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Время смерти токена
                .signWith(secretKey)                     // Подписываем нашим секретным ключом
                .compact();                              // Склеиваем в одну длинную строку
    }

    // Метод, который вытаскивает логин из прилетевшего токена
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Метод проверки: не подделан ли токен и не истекло ли его время жизни?
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Если токен просрочен или подделан — метод просто вернет false
            return false;
        }
    }
}
