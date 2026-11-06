package com.sumpositive.ticketing.dashboard.service;

import com.sumpositive.ticketing.dashboard.dto.Dashboard;
import com.sumpositive.ticketing.ticket.entity.TicketStatus;
import com.sumpositive.ticketing.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TicketRepository ticketRepository;

    public DashboardService(TicketRepository ticketRepository) {
		super();
		this.ticketRepository = ticketRepository;
	}

	public Dashboard getSummary() {

        long open = ticketRepository.countByStatus(
            TicketStatus.OPEN
        );

        long inProgress = ticketRepository.countByStatus(
            TicketStatus.IN_PROGRESS
        );

        long resolved = ticketRepository.countByStatus(
            TicketStatus.RESOLVED
        );

        long closed = ticketRepository.countByStatus(
            TicketStatus.CLOSED
        );

        return new Dashboard(
            open + inProgress + resolved + closed,
            open,
            inProgress,
            resolved,
            closed
        );
    }
}
