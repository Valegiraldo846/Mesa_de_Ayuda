package com.sena.database_connection.mesadeayuda.controller;

import com.sena.database_connection.mesadeayuda.dtos.*;
import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.security.UsuarioPrincipal;
import com.sena.database_connection.mesadeayuda.service.TicketsService;
import com.sena.database_connection.mesadeayuda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketsService ticketsService;
    private final UsuarioService usuarioService;

    public TicketController(TicketsService ticketsService, UsuarioService usuarioService) {
        this.ticketsService = ticketsService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(
            @Valid @RequestBody TicketDto ticketDto,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getEmail());
        Ticket ticket = ticketsService.crearTicket(ticketDto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    @GetMapping("/mios")
    public ResponseEntity<List<TicketResponseDto>> misTickets(
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getEmail());
        return ResponseEntity.ok(ticketsService.listarMisTickets(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> obtenerTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getEmail());
        return ResponseEntity.ok(ticketsService.obtenerTicketPorId(id, usuario));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> listarTodos() {
        return ResponseEntity.ok(ticketsService.listarTodosLosTickets());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketResponseDto> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(ticketsService.cambiarEstado(id, request.getEstado()));
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<TicketResponseDto>> ticketsVencidos() {
        return ResponseEntity.ok(ticketsService.listarTicketsVencidos());
    }
}
