package com.roma.apitelegramstore.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring сам сгенерирует SQL запрос: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}
