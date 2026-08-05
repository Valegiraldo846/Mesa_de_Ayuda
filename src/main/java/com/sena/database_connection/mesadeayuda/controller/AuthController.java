package com.sena.database_connection.mesadeayuda.controller;

import com.sena.database_connection.mesadeayuda.dtos.RegistroRequest;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping()
    public ResponseEntity<Usuario> registrar(
            @Valid @RequestBody RegistroRequest registroRequest) {

        if (usuarioService.registrarUsuario(registroRequest) == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(registroRequest));
    }
}