package ma.ac.exam.helpdeskapi.web.dto.ticket;

import jakarta.validation.constraints.NotNull;

public record AssignTicketRequest(@NotNull Long agentId) {
}
