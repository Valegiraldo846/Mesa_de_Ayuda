package com.sena.database_connection.mesadeayuda.repository;

import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreadoPor(Usuario usuario);
}