package com.sena.database_connection.mesadeayuda.service;

import com.sena.database_connection.mesadeayuda.entities.RefreshToken;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.exception.RefreshTokenInvalidoException;
import com.sena.database_connection.mesadeayuda.repository.RefreshTokenRepository;
import com.sena.database_connection.mesadeayuda.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RefreshToken crearRefreshToken(Usuario usuario) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(jwtUtil.generarRefreshToken(usuario.getEmail()));
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiraEn(LocalDateTime.now().plusDays(7));
        refreshToken.setRevocado(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validarRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenInvalidoException("Refresh token no encontrado"));

        if (refreshToken.isRevocado()) {
            throw new RefreshTokenInvalidoException("Refresh token revocado");
        }

        if (refreshToken.getExpiraEn().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenInvalidoException("Refresh token expirado");
        }

        return refreshToken;
    }

    @Transactional
    public void revocarRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenInvalidoException("Refresh token no encontrado"));
        refreshToken.setRevocado(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revocarTodosDelUsuario(Usuario usuario) {
        var tokens = refreshTokenRepository.findByUsuario(usuario);
        tokens.forEach(t -> t.setRevocado(true));
        refreshTokenRepository.saveAll(tokens);
    }
}
