package com.sena.database_connection.mesadeayuda.repository;

import com.sena.database_connection.mesadeayuda.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario,Long>
{
    Usuario findByEmail(String email);
}
