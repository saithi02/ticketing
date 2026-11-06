package com.sumpositive.ticketing.ticket.service;
import com.sumpositive.ticketing.ticket.dto.*;
import com.sumpositive.ticketing.ticket.entity.*;
import com.sumpositive.ticketing.ticket.mapper.TicketMapper;
import com.sumpositive.ticketing.ticket.repository.TicketRepository;
import com.sumpositive.ticketing.user.entity.User;
import com.sumpositive.ticketing.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository,
			TicketMapper ticketMapper) {
		super();
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.ticketMapper = ticketMapper;
	}

	private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> getAll(String username) {

        User user = findUser(username);

        if (hasRole(user, "ROLE_ADMIN")) {
            return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .toList();
        }

        if (hasRole(user, "ROLE_AGENT")) {
            return ticketRepository.findByAssignedToId(user.getId())
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
        }

        return ticketRepository.findByCreatedById(user.getId())
            .stream()
            .map(ticketMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getById(Long id, String username) {

        Ticket ticket = findTicket(id);
        User user = findUser(username);

        checkCanView(ticket, user);

        return ticketMapper.toResponse(ticket);
    }

    @Override
    public TicketResponse create(
        TicketRequest request,
        String username
    ) {

        User creator = findUser(username);

        Ticket ticket = new Ticket();

        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(creator);

        return ticketMapper.toResponse(
            ticketRepository.save(ticket)
        );
    }

    @Override
    public TicketResponse update(
        Long id,
        TicketRequest request,
        String username
    ) {

        Ticket ticket = findTicket(id);
        User user = findUser(username);

        if (!hasRole(user, "ROLE_ADMIN") &&
            !ticket.getCreatedBy().getId().equals(user.getId())) {

            throw new AccessDeniedException(
                "You cannot update this ticket"
            );
        }

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException(
                "Closed tickets cannot be edited"
            );
        }

        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());

        return ticketMapper.toResponse(
            ticketRepository.save(ticket)
        );
    }

    @Override
    public TicketResponse assign(
        Long id,
        TicketAssignmentRequest request
    ) {

        Ticket ticket = findTicket(id);

        User assignee = userRepository.findById(request.userId())
            .orElseThrow(() ->
                new IllegalArgumentException("Assignee not found")
            );

        if (!assignee.isActive() ||
            (!hasRole(assignee, "ROLE_AGENT") &&
             !hasRole(assignee, "ROLE_ADMIN"))) {

            throw new IllegalArgumentException(
                "Assignee must be an active agent or administrator"
            );
        }

        ticket.setAssignedTo(assignee);

        return ticketMapper.toResponse(
            ticketRepository.save(ticket)
        );
    }

    @Override
    public TicketResponse updateStatus(
        Long id,
        TicketStatusRequest request,
        String username
    ) {

        Ticket ticket = findTicket(id);
        User user = findUser(username);

        boolean admin = hasRole(user, "ROLE_ADMIN");

        boolean assignedAgent =
            hasRole(user, "ROLE_AGENT") &&
            ticket.getAssignedTo() != null &&
            ticket.getAssignedTo().getId().equals(user.getId());

        if (!admin && !assignedAgent) {
            throw new AccessDeniedException(
                "Only administrators or assigned agents can update status"
            );
        }

        ticket.setStatus(request.status());

        return ticketMapper.toResponse(
            ticketRepository.save(ticket)
        );
    }

    @Override
    public void delete(Long id) {

        Ticket ticket = findTicket(id);

        ticketRepository.delete(ticket);
    }
    @Override
    public void delete(Long id, String username) {

        // Find ticket
        Ticket ticket = findTicket(id);

        // Find logged-in user
        User user = findUser(username);

        // Check admin role
        boolean admin = hasRole(user, "ROLE_ADMIN");

        // Check ticket ownership
        boolean creator =
                ticket.getCreatedBy()
                      .getId()
                      .equals(user.getId());

        // Allow admin or ticket creator
        if (!admin && !creator) {
            throw new AccessDeniedException(
                    "You can only delete your own tickets"
            );
        }

        ticketRepository.delete(ticket);
    }

    private Ticket findTicket(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Ticket not found")
            );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() ->
                new IllegalArgumentException("User not found")
            );
    }

    private boolean hasRole(User user, String role) {
        return user.getRoles().stream()
            .anyMatch(r -> r.getName().equals(role));
    }

    private void checkCanView(Ticket ticket, User user) {

        boolean admin = hasRole(user, "ROLE_ADMIN");

        boolean creator =
            ticket.getCreatedBy().getId().equals(user.getId());

        boolean assignee =
            ticket.getAssignedTo() != null &&
            ticket.getAssignedTo().getId().equals(user.getId());

        if (!admin && !creator && !assignee) {
            throw new AccessDeniedException(
                "You cannot access this ticket"
            );
        }
    }
}
