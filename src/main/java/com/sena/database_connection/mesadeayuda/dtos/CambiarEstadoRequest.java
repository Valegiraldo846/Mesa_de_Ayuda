package com.sena.database_connection.mesadeayuda.dtos;

import com.sena.database_connection.mesadeayuda.enums.Estado;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoRequest {
    @NotNull
    private Estado estado;
}
