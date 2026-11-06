package com.sumpositive.ticketing.ticket.mapper;


import com.sumpositive.ticketing.ticket.entity.Ticket;
import com.sumpositive.ticketing.ticket.dto.TicketResponse;

import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket) {

        return new TicketResponse(
            ticket.getId(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getPriority(),

            ticket.getCreatedBy().getId(),
            ticket.getCreatedBy().getUsername(),

            ticket.getAssignedTo() != null
                ? ticket.getAssignedTo().getId()
                : null,

            ticket.getAssignedTo() != null
                ? ticket.getAssignedTo().getUsername()
                : null,

            ticket.getCreatedAt(),
            ticket.getUpdatedAt()
        );
    }
}
