package com.sumpositive.ticketing.ticket.dto;

import jakarta.validation.constraints.NotNull;

public record TicketAssignmentRequest(

    @NotNull
    Long userId

) {}
