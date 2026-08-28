package com.sena.database_connection.mesadeayuda.controller;

import com.sena.database_connection.mesadeayuda.dtos.AscenderRequest;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/soporte")
    public ResponseEntity<Map<String, String>> ascenderASoporte(
            @Valid @RequestBody AscenderRequest request) {
        Usuario usuario = usuarioService.ascenderASoporte(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario " + usuario.getEmail() + " ascendido a SOPORTE"
        ));
    }
}
