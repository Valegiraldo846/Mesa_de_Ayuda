package com.sena.database_connection.mesadeayuda.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AscenderRequest {
    @NotBlank
    @Email
    private String email;
}
