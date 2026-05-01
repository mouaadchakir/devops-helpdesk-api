package ma.ac.exam.helpdeskapi.config;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import ma.ac.exam.helpdeskapi.domain.entity.AppUser;
import ma.ac.exam.helpdeskapi.domain.entity.Role;
import ma.ac.exam.helpdeskapi.domain.enums.RoleName;
import ma.ac.exam.helpdeskapi.repository.RoleRepository;
import ma.ac.exam.helpdeskapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            ensureRole(RoleName.ROLE_USER);
            ensureRole(RoleName.ROLE_AGENT);
            ensureRole(RoleName.ROLE_ADMIN);

            createUserIfMissing("user", "user@helpdesk.local", "user123", Set.of(RoleName.ROLE_USER));
            createUserIfMissing("agent", "agent@helpdesk.local", "agent123", Set.of(RoleName.ROLE_AGENT));
            createUserIfMissing("admin", "admin@helpdesk.local", "admin123", Set.of(RoleName.ROLE_ADMIN));
        };
    }

    private void ensureRole(RoleName roleName) {
        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(Role.builder().name(roleName).build());
        }
    }

    private void createUserIfMissing(String username, String email, String rawPassword, Set<RoleName> roles) {
        if (userRepository.existsByUsername(username)) {
            return;
        }

        Set<Role> mappedRoles = roleRepository.findAll().stream()
                .filter(role -> roles.contains(role.getName()))
                .collect(java.util.stream.Collectors.toSet());

        AppUser user = AppUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .roles(mappedRoles)
                .build();

        userRepository.save(user);
    }
}
