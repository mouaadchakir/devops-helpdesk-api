package ma.ac.exam.helpdeskapi.web.dto.auth;

import java.util.Set;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        Set<String> roles
) {
}
