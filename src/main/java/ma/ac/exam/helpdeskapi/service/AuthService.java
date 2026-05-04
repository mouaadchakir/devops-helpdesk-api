package ma.ac.exam.helpdeskapi.service;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ma.ac.exam.helpdeskapi.domain.entity.AppUser;
import ma.ac.exam.helpdeskapi.domain.entity.Role;
import ma.ac.exam.helpdeskapi.domain.enums.RoleName;
import ma.ac.exam.helpdeskapi.repository.RoleRepository;
import ma.ac.exam.helpdeskapi.repository.UserRepository;
import ma.ac.exam.helpdeskapi.web.dto.auth.RegisterRequest;
import ma.ac.exam.helpdeskapi.web.dto.auth.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ROLE_USER not found"));

        AppUser user = AppUser.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(userRole))
                .build();

        AppUser saved = userRepository.save(user);
        return toProfile(saved);
    }

    public UserProfileResponse getProfile(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toProfile(user);
    }

    private UserProfileResponse toProfile(AppUser user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet())
        );
    }
}
