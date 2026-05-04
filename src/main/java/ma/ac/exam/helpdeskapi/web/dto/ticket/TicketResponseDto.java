package ma.ac.exam.helpdeskapi.web.dto.ticket;

import java.time.LocalDateTime;

public record TicketResponseDto(
        Long id,
        String message,
        String authorUsername,
        LocalDateTime createdAt
) {
}
