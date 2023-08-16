package com.remidiousE.repository;

import com.remidiousE.model.PasswordRestToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordRestToken, Long> {
    PasswordRestToken findByToken(String token);
}
