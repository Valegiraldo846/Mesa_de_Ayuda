package com.sena.database_connection.mesadeayuda.controller;

import com.sena.database_connection.mesadeayuda.dtos.TicketDto;
import com.sena.database_connection.mesadeayuda.entities.Ticket;
import com.sena.database_connection.mesadeayuda.service.TicketsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket/")
public class TicketController {
    private final TicketsService ticketService;
    public TicketController(TicketsService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping()
    public ResponseEntity<Ticket> registrarTicket(
            @Valid @RequestBody TicketDto ticketDto
    ){

    }
}
