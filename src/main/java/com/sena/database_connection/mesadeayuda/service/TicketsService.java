package com.sena.database_connection.mesadeayuda.service;

import com.sena.database_connection.mesadeayuda.dtos.TicketDto;
import com.sena.database_connection.mesadeayuda.dtos.TicketResponseDto;
import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.entities.Usuario;
import com.sena.database_connection.mesadeayuda.enums.Estado;
import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import com.sena.database_connection.mesadeayuda.enums.Rol;
import com.sena.database_connection.mesadeayuda.exception.AccesoNoPermitidoException;
import com.sena.database_connection.mesadeayuda.exception.RecursoNoEncontradoException;
import com.sena.database_connection.mesadeayuda.repository.TicketsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;

    public TicketsService(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    public Ticket crearTicket(TicketDto ticketDto, Usuario usuario) {
        Ticket ticket = new Ticket();
        ticket.setTitulo(ticketDto.getTitulo());
        ticket.setDescripcion(ticketDto.getDescripcion());
        ticket.setPrioridad(ticketDto.getPrioridad());
        ticket.setEstado(Estado.ABIERTO);
        ticket.setCreadoEn(LocalDateTime.now());
        ticket.setCreadoPor(usuario);

        switch (ticketDto.getPrioridad()) {
            case ALTA -> ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(4));
            case MEDIA -> ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(24));
            case BAJA -> ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(72));
        }

        return ticketsRepository.save(ticket);
    }

    public List<TicketResponseDto> listarMisTickets(Usuario usuario) {
        return ticketsRepository.findByCreadoPor(usuario).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TicketResponseDto obtenerTicketPorId(Long id, Usuario usuario) {
        Ticket ticket = ticketsRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado con id: " + id));

        if (usuario.getRol() == Rol.USUARIO && !ticket.getCreadoPor().getId().equals(usuario.getId())) {
            throw new AccesoNoPermitidoException("No tiene permiso para consultar este ticket");
        }

        return toResponseDto(ticket);
    }

    public List<TicketResponseDto> listarTodosLosTickets() {
        return ticketsRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TicketResponseDto cambiarEstado(Long id, Estado nuevoEstado) {
        Ticket ticket = ticketsRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado con id: " + id));

        ticket.setEstado(nuevoEstado);
        ticketsRepository.save(ticket);

        return toResponseDto(ticket);
    }

    public List<TicketResponseDto> listarTicketsVencidos() {
        return ticketsRepository.findVencidos(LocalDateTime.now()).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private TicketResponseDto toResponseDto(Ticket ticket) {
        boolean vencido = ticket.getEstado() != Estado.RESUELTO
                && ticket.getSlaVenceEn() != null
                && LocalDateTime.now().isAfter(ticket.getSlaVenceEn());

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getPrioridad(),
                ticket.getEstado(),
                ticket.getCreadoEn(),
                ticket.getSlaVenceEn(),
                ticket.getCreadoPor().getId(),
                ticket.getCreadoPor().getNombre(),
                vencido
        );
    }
}
