package com.sena.database_connection.mesadeayuda.service;

import com.sena.database_connection.mesadeayuda.dtos.RegistroRequest;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.enums.Rol;
import com.sena.database_connection.mesadeayuda.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario registrarUsuario(RegistroRequest registroRequest) {

        Usuario usuario = new Usuario();

        usuario.setNombre(registroRequest.getNombre());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(registroRequest.getPassword());

        Usuario usuarioExistente = buscarPorEmail(usuario.getEmail());

        if (usuarioExistente != null) {
            return null;
        }

        String passwordCifrada = passwordEncoder.encode(usuario.getPassword());

        usuario.setPassword(passwordCifrada);
        usuario.setRol(Rol.USUARIO);

        return usuarioRepository.save(usuario);
    }
}