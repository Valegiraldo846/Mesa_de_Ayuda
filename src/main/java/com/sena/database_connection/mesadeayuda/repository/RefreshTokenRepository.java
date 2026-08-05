package com.sena.database_connection.mesadeayuda.repository;

import com.sena.database_connection.mesadeayuda.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String refreshToken);
}
