package ma.ac.exam.helpdeskapi.repository;

import java.util.List;
import ma.ac.exam.helpdeskapi.domain.entity.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long> {
    List<TicketResponse> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
