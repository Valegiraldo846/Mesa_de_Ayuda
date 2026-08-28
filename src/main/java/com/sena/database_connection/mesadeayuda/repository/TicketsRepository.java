package com.sena.database_connection.mesadeayuda.repository;

import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreadoPor(Usuario usuario);

    @Query("SELECT t FROM Ticket t WHERE t.estado != 'RESUELTO' AND t.slaVenceEn < :ahora")
    List<Ticket> findVencidos(LocalDateTime ahora);
}
