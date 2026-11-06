package com.sumpositive.ticketing.ticket.dto;


import com.sumpositive.ticketing.ticket.entity.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketRequest(

    @NotBlank
    @Size(max = 255)
    String title,

    @Size(max = 2000)
    String description,

    @NotNull
    Priority priority

) {}
