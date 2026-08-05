package com.sena.database_connection.mesadeayuda.service;


import com.sena.database_connection.mesadeayuda.dtos.TicketDto;
import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.enums.Estado;
import com.sena.database_connection.mesadeayuda.enums.Prioridad;
import com.sena.database_connection.mesadeayuda.repository.TicketsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketsService {
    private final TicketsRepository ticketsRepository;

    public TicketsService(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    public Ticket regitrarTicket(TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ticket.setTitulo(ticketDto.titulo());
        ticket.setDescripcion(ticketDto.descripcion());
        ticket.setPrioridad(ticketDto.prioridad());
        ticket.setCreadoEn(LocalDateTime.now());
        ticket.setEstado(Estado.ABIERTO);

        switch (ticketDto.prioridad()) {
            case ALTA:
                ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(4));

                break;

            case MEDIA:
                ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(24));
                break;

            case BAJA:
                ticket.setSlaVenceEn(ticket.getCreadoEn().plusHours(72));
                break;

        }
       return ticketsRepository.save(ticket);


    }

}
