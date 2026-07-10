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
                        // 1. ОТКРЫВАЕМ СИСТЕМНЫЕ ПУТИ ОШИБОК (Критически важно!)
                        // Если внутри кода что-то пойдет не так, Спринг покажет текст ошибки, а не глупую 401
                        .requestMatchers("/error", "/error/**").permitAll()

                        // 2. Полностью открываем всю папку auth для любых методов (GET, POST)
                        // Звездочки /** гарантируют, что и /api/auth/register, и любые другие ссылки тут будут открыты
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. Разрешаем всем просматривать товары (GET)
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // 4. Разрешаем всем делать заказы (POST)
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()

                        // 5. Создавать и удалять товары может только админ
                        .requestMatchers("/api/products/**").hasRole("ADMIN")

                        // 6. Всё остальное требует авторизации
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    //для телеграма, лалала
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
