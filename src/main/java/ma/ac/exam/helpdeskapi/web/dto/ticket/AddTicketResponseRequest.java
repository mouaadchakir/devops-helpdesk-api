package ma.ac.exam.helpdeskapi.web.dto.ticket;

import jakarta.validation.constraints.NotBlank;

public record AddTicketResponseRequest(@NotBlank String message) {
}
