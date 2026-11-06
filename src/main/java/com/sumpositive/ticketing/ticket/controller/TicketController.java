package com.sumpositive.ticketing.ticket.controller;


import com.sumpositive.ticketing.ticket.dto.*;
import com.sumpositive.ticketing.ticket.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    public TicketController(TicketService ticketService) {
		super();
		this.ticketService = ticketService;
	}

	private final TicketService ticketService;

    @GetMapping
    public List<TicketResponse> getAll(
        Authentication authentication
    ) {
        return ticketService.getAll(authentication.getName());
    }

    @GetMapping("/{id}")
    public TicketResponse getById(
        @PathVariable Long id,
        Authentication authentication
    ) {
        return ticketService.getById(
            id,
            authentication.getName()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(
        @Valid @RequestBody TicketRequest request,
        Authentication authentication
    ) {
        return ticketService.create(
            request,
            authentication.getName()
        );
    }

    @PutMapping("/{id}")
    public TicketResponse update(
        @PathVariable Long id,
        @Valid @RequestBody TicketRequest request,
        Authentication authentication
    ) {
        return ticketService.update(
            id,
            request,
            authentication.getName()
        );
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public TicketResponse assign(
        @PathVariable Long id,
        @Valid @RequestBody TicketAssignmentRequest request
    ) {
        return ticketService.assign(id, request);
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(
        @PathVariable Long id,
        @Valid @RequestBody TicketStatusRequest request,
        Authentication authentication
    ) {
        return ticketService.updateStatus(
            id,
            request,
            authentication.getName()
        );
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        ticketService.delete(id, authentication.getName());

        return ResponseEntity.ok("Ticket deleted successfully");
    }
    
}
