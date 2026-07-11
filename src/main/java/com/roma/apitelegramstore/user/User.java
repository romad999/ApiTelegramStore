package com.roma.apitelegramstore.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;


    // --- МЕТОДЫ ИНТЕРФЕЙСА USERDETAILS (ТЗ от Spring Security) ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Если в поле role хранится "ADMIN", превращаем в "ROLE_ADMIN"
        // Если там уже сохранено "ROLE_ADMIN", то оставляем как есть
        String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(prefixedRole));
    }


    // Все проверки ниже мы просто возвращаем как true, чтобы аккаунт всегда был активен
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
