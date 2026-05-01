package ma.ac.exam.helpdeskapi.web.dto.ticket;

import java.time.LocalDateTime;
import java.util.List;
import ma.ac.exam.helpdeskapi.domain.enums.TicketPriority;
import ma.ac.exam.helpdeskapi.domain.enums.TicketStatus;

public record TicketDto(
        Long id,
        String title,
        String description,
        TicketPriority priority,
        TicketStatus status,
        String createdByUsername,
        String assignedAgentUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TicketResponseDto> responses
) {
}
