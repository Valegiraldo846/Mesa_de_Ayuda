package com.sena.database_connection.mesadeayuda.entities;

import com.sena.database_connection.mesadeayuda.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "creadoPor")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "usuario")
    private List<RefreshToken> refreshTokens;
}
