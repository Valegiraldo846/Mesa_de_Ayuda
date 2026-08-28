package com.sena.database_connection.mesadeayuda.repository;

import com.sena.database_connection.mesadeayuda.entities.RefreshToken;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUsuario(Usuario usuario);
}
