package com.sena.database_connection.mesadeayuda.service;

import com.sena.database_connection.mesadeayuda.dtos.LoginRequest;
import com.sena.database_connection.mesadeayuda.dtos.LoginResponse;
import com.sena.database_connection.mesadeayuda.dtos.RefreshTokenRequest;
import com.sena.database_connection.mesadeayuda.dtos.RegistroRequest;
import com.sena.database_connection.mesadeayuda.entities.RefreshToken;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.exception.CredencialesInvalidasException;
import com.sena.database_connection.mesadeayuda.security.JwtUtil;
import com.sena.database_connection.mesadeayuda.security.UsuarioPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UsuarioService usuarioService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService,
                       UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.usuarioService = usuarioService;
    }

    public Usuario registrar(RegistroRequest registroRequest) {
        return usuarioService.registrarUsuario(registroRequest);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        Usuario usuario = usuarioService.buscarPorEmail(principal.getEmail());

        String accessToken = jwtUtil.generarAccessToken(usuario.getEmail(), usuario.getRol().name());
        RefreshToken refreshToken = refreshTokenService.crearRefreshToken(usuario);

        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    public LoginResponse refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenService.validarRefreshToken(refreshTokenValue);
        Usuario usuario = refreshToken.getUsuario();

        String nuevoAccessToken = jwtUtil.generarAccessToken(usuario.getEmail(), usuario.getRol().name());

        return new LoginResponse(nuevoAccessToken, refreshToken.getToken());
    }

    public void logout(String refreshTokenValue) {
        refreshTokenService.revocarRefreshToken(refreshTokenValue);
    }
}
