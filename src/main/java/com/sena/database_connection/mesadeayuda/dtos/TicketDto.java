package com.sena.database_connection.mesadeayuda.dtos;

import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketDto {
    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    private Prioridad prioridad;
}
