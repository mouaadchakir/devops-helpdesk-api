package ma.ac.exam.helpdeskapi.repository;

import java.util.Optional;
import ma.ac.exam.helpdeskapi.domain.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
