package ma.ac.exam.helpdeskapi.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ma.ac.exam.helpdeskapi.service.TicketService;
import ma.ac.exam.helpdeskapi.web.dto.ticket.AddTicketResponseRequest;
import ma.ac.exam.helpdeskapi.web.dto.ticket.AssignTicketRequest;
import ma.ac.exam.helpdeskapi.web.dto.ticket.CreateTicketRequest;
import ma.ac.exam.helpdeskapi.web.dto.ticket.TicketDto;
import ma.ac.exam.helpdeskapi.web.dto.ticket.TicketResponseDto;
import ma.ac.exam.helpdeskapi.web.dto.ticket.UpdateTicketStatusRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto createTicket(@Valid @RequestBody CreateTicketRequest request, Authentication authentication) {
        return ticketService.createTicket(authentication.getName(), request);
    }

    @GetMapping
    public List<TicketDto> listTickets(Authentication authentication) {
        return ticketService.getAccessibleTickets(authentication.getName());
    }

    @GetMapping("/{ticketId}")
    public TicketDto getTicket(@PathVariable Long ticketId, Authentication authentication) {
        return ticketService.getTicketById(ticketId, authentication.getName());
    }

    @PatchMapping("/{ticketId}/assign")
    public TicketDto assignTicket(@PathVariable Long ticketId, @Valid @RequestBody AssignTicketRequest request) {
        return ticketService.assignTicket(ticketId, request.agentId());
    }

    @PatchMapping("/{ticketId}/status")
    public TicketDto updateStatus(
            @PathVariable Long ticketId,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        return ticketService.updateStatus(ticketId, request.status());
    }

    @PostMapping("/{ticketId}/responses")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponseDto addResponse(
            @PathVariable Long ticketId,
            @Valid @RequestBody AddTicketResponseRequest request,
            Authentication authentication
    ) {
        return ticketService.addResponse(ticketId, authentication.getName(), request.message());
    }
}
