package ma.ac.exam.helpdeskapi.web.dto.ticket;

import jakarta.validation.constraints.NotNull;
import ma.ac.exam.helpdeskapi.domain.enums.TicketStatus;

public record UpdateTicketStatusRequest(@NotNull TicketStatus status) {
}
