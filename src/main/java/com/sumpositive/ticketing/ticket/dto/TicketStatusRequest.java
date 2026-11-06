package com.sumpositive.ticketing.ticket.dto;

import com.sumpositive.ticketing.ticket.entity.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusRequest(

    @NotNull
    TicketStatus status

) {}
