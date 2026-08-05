package com.sena.database_connection.mesadeayuda.dtos;

import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public record TicketDto(
        @NotBlank
        String titulo,
        @NotBlank
        String descripcion,
        @NotBlank
        Prioridad prioridad,
        @NotBlank
        LocalDateTime creadoEn) {

}
