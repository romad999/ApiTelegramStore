package com.roma.apitelegramstore.config;

import com.roma.apitelegramstore.user.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

// тут гора импортов

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable) // По-прежнему отключаем CSRF

                .authorizeHttpRequests(auth -> auth
                         // 1. Системные пути и авторизация доступны всем
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                         // 2. Витрину товаров (GET) могут смотреть ВСЕ (и гости, и юзеры, и админ)
                         .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                         // 3. Создавать заказы (POST) и смотреть свою историю (GET /api/orders/history)
                         // могут ТОЛЬКО авторизованные ПОКУПАТЕЛИ (или админ, если захочет)
                         .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")

                         // 4. Управление товарами (POST, PUT, DELETE) — ТОЛЬКО для ВЛАДЕЛЬЦА (ADMIN)
                         // Важно: Spring Security при проверке hasRole("ADMIN") будет искать в БД роль "ROLE_ADMIN".
                         .requestMatchers("/api/products/**").hasRole("ADMIN")

                         // 5. Всё остальное требует железной авторизации
                         .anyRequest().authenticated()
                )
                // ДОБАВЛЯЕМ НАШ ФИЛЬТР ТОКЕНОВ В ЦЕПОЧКУ ОХРАНЫ С ПРИОРИТЕТОМ!
                .addFilterBefore(jwtTokenFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    // BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Этот инструмент будет автоматически шифровать пароли по алгоритму BCrypt
        return new BCryptPasswordEncoder();
    }

    // хз че это
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }


    // какой то бин для телеграма
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
