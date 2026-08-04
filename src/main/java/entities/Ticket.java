package entities;

import enums.Estado;
import enums.Prioridad;

import java.time.LocalDateTime;

public class Ticket {
    private long id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;
    private LocalDateTime creadoEn;
    private LocalDateTime slaVenceEn;
    private Usuario creadoPor;
}
