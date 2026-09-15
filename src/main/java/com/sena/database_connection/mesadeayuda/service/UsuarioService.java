package com.sena.database_connection.mesadeayuda.service;

import com.sena.database_connection.mesadeayuda.dtos.RegistroRequest;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.enums.Rol;
import com.sena.database_connection.mesadeayuda.exception.EmailYaRegistradoException;
import com.sena.database_connection.mesadeayuda.exception.RecursoNoEncontradoException;
import com.sena.database_connection.mesadeayuda.repository.UsuarioRepository;
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

    public Usuario registrarUsuario(RegistroRequest registroRequest) {
        Usuario existente = usuarioRepository.findByEmail(registroRequest.getEmail());
        if (existente != null) {
            throw new EmailYaRegistradoException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroRequest.getNombre());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        usuario.setRol(registroRequest.getRol() != null ? registroRequest.getRol() : Rol.USUARIO);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con email: " + email);
        }
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    public Usuario ascenderASoporte(String email) {
        Usuario usuario = buscarPorEmail(email);
        usuario.setRol(Rol.SOPORTE);
        return usuarioRepository.save(usuario);
    }
}
