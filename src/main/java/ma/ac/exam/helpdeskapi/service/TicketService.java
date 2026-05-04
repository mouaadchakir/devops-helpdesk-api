package ma.ac.exam.helpdeskapi.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ma.ac.exam.helpdeskapi.domain.entity.AppUser;
import ma.ac.exam.helpdeskapi.domain.entity.Ticket;
import ma.ac.exam.helpdeskapi.domain.entity.TicketResponse;
import ma.ac.exam.helpdeskapi.domain.enums.RoleName;
import ma.ac.exam.helpdeskapi.domain.enums.TicketPriority;
import ma.ac.exam.helpdeskapi.repository.TicketRepository;
import ma.ac.exam.helpdeskapi.repository.TicketResponseRepository;
import ma.ac.exam.helpdeskapi.repository.UserRepository;
import ma.ac.exam.helpdeskapi.web.dto.ticket.CreateTicketRequest;
import ma.ac.exam.helpdeskapi.web.dto.ticket.TicketDto;
import ma.ac.exam.helpdeskapi.web.dto.ticket.TicketResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketResponseRepository ticketResponseRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketDto createTicket(String username, CreateTicketRequest request) {
        AppUser creator = getUserByUsername(username);
        Ticket ticket = Ticket.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority() == null ? TicketPriority.MEDIUM : request.priority())
                .createdBy(creator)
                .build();
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<TicketDto> getAccessibleTickets(String username) {
        AppUser currentUser = getUserByUsername(username);

        List<Ticket> tickets;
        if (hasAnyRole(currentUser, Set.of(RoleName.ROLE_ADMIN))) {
            tickets = ticketRepository.findAll();
        } else if (hasAnyRole(currentUser, Set.of(RoleName.ROLE_AGENT))) {
            tickets = ticketRepository.findByAssignedAgentId(currentUser.getId());
        } else {
            tickets = ticketRepository.findByCreatedById(currentUser.getId());
        }

        return tickets.stream()
                .sorted(Comparator.comparing(Ticket::getCreatedAt).reversed())
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketDto getTicketById(Long ticketId, String username) {
        AppUser currentUser = getUserByUsername(username);
        Ticket ticket = getTicket(ticketId);

        boolean canAccess = hasAnyRole(currentUser, Set.of(RoleName.ROLE_ADMIN))
                || ticket.getCreatedBy().getId().equals(currentUser.getId())
                || (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getId().equals(currentUser.getId()));
        if (!canAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this ticket");
        }

        return toDto(ticket);
    }

    @Transactional
    public TicketDto assignTicket(Long ticketId, Long agentId) {
        Ticket ticket = getTicket(ticketId);
        AppUser agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent not found"));

        if (!hasAnyRole(agent, Set.of(RoleName.ROLE_AGENT, RoleName.ROLE_ADMIN))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected user is not an agent/admin");
        }

        ticket.setAssignedAgent(agent);
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDto updateStatus(Long ticketId, ma.ac.exam.helpdeskapi.domain.enums.TicketStatus status) {
        Ticket ticket = getTicket(ticketId);
        ticket.setStatus(status);
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponseDto addResponse(Long ticketId, String agentUsername, String message) {
        Ticket ticket = getTicket(ticketId);
        AppUser author = getUserByUsername(agentUsername);

        TicketResponse response = TicketResponse.builder()
                .ticket(ticket)
                .author(author)
                .message(message)
                .build();

        return toResponseDto(ticketResponseRepository.save(response));
    }

    private AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    private boolean hasAnyRole(AppUser user, Set<RoleName> roles) {
        return user.getRoles().stream().anyMatch(role -> roles.contains(role.getName()));
    }

    private TicketDto toDto(Ticket ticket) {
        List<TicketResponseDto> responses = ticketResponseRepository.findByTicketIdOrderByCreatedAtAsc(ticket.getId())
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return new TicketDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedBy().getUsername(),
                ticket.getAssignedAgent() == null ? null : ticket.getAssignedAgent().getUsername(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                responses
        );
    }

    private TicketResponseDto toResponseDto(TicketResponse response) {
        return new TicketResponseDto(
                response.getId(),
                response.getMessage(),
                response.getAuthor().getUsername(),
                response.getCreatedAt()
        );
    }
}
