package com.sumpositive.ticketing.ticket.repository;


import com.sumpositive.ticketing.ticket.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreatedById(Long userId);

    List<Ticket> findByAssignedToId(Long userId);

    long countByStatus(TicketStatus status);

    long countByCreatedById(Long userId);

    long countByAssignedToId(Long userId);
}


