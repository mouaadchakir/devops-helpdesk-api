package ma.ac.exam.helpdeskapi.repository;

import java.util.List;
import ma.ac.exam.helpdeskapi.domain.entity.Ticket;
import ma.ac.exam.helpdeskapi.domain.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByAssignedAgentId(Long assignedAgentId);
    List<Ticket> findByCreatedById(Long createdById);
}
