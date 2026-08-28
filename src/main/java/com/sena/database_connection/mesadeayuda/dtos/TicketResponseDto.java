package com.sena.database_connection.mesadeayuda.dtos;

import com.sena.database_connection.mesadeayuda.enums.Estado;
import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketResponseDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;
    private LocalDateTime creadoEn;
    private LocalDateTime slaVenceEn;
    private Long creadoPorId;
    private String creadoPorNombre;
    private boolean vencido;
}
