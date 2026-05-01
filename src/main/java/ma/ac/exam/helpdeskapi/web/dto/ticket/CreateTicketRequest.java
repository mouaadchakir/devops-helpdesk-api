package ma.ac.exam.helpdeskapi.web.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ma.ac.exam.helpdeskapi.domain.enums.TicketPriority;

public record CreateTicketRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank String description,
        TicketPriority priority
) {
}
