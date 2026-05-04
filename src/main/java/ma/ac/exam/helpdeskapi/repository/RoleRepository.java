package ma.ac.exam.helpdeskapi.repository;

import java.util.Optional;
import ma.ac.exam.helpdeskapi.domain.entity.Role;
import ma.ac.exam.helpdeskapi.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
