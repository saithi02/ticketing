package com.sumpositive.ticketing.ticket.dto;


import com.sumpositive.ticketing.ticket.entity.*;

import java.time.LocalDateTime;

public record TicketResponse(

    Long id,
    String title,
    String description,
    TicketStatus status,
    Priority priority,

    Long createdById,
    String createdBy,

    Long assignedToId,
    String assignedTo,

    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {}
