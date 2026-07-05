package com.roma.apitelegramstore.user;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter { // Однократный фильтр на каждый запрос

    private final JwtCore jwtCore;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenFilter(JwtCore jwtCore, CustomUserDetailsService userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Вытаскиваем заголовок Authorization из запроса
            String headerAuth = request.getHeader("Authorization");
            String jwt = null;

            // Token должен начинаться со слова Bearer
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7); // Отрезаем слово "Bearer " и берем чистый токен
            }

            // 2. Если токен есть и он валидный — авторизуем пользователя
            if (jwt != null && jwtCore.validateJwtToken(jwt)) {
                String username = jwtCore.getUsernameFromJwtToken(jwt); // Достаем логин из токена

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Ищем в базе

                // Создаем официальный документ авторизации внутри Spring
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Кладем этот документ в Спринг-контекст. Пограничник увидит его и пропустит!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Если что-то пошло не так — просто не авторизуем, сработает защита
        }

        // Пропускаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}

// я устал, идите нахер