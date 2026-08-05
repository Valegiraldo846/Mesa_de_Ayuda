package com.sena.database_connection.mesadeayuda.entities;

import com.sena.database_connection.mesadeayuda.enums.Estado;
import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDateTime creadoEn;

    private LocalDateTime slaVenceEn;

    @ManyToOne
    @JoinColumn(name = "creado_por_id")
    private Usuario creadoPor;
}