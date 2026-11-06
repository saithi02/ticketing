package com.sumpositive.ticketing.ticket.service;

import com.sumpositive.ticketing.ticket.dto.*;

import java.util.List;

public interface TicketService {

    List<TicketResponse> getAll(String username);

    TicketResponse getById(Long id, String username);

    TicketResponse create(
        TicketRequest request,
        String username
    );

    TicketResponse update(
        Long id,
        TicketRequest request,
        String username
    );

    TicketResponse assign(
        Long id,
        TicketAssignmentRequest request
    );

    TicketResponse updateStatus(
        Long id,
        TicketStatusRequest request,
        String username
    );

    void delete(Long id);
    void delete(Long id, String username);
}
