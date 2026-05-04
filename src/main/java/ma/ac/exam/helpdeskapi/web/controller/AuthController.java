package ma.ac.exam.helpdeskapi.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ac.exam.helpdeskapi.service.AuthService;
import ma.ac.exam.helpdeskapi.web.dto.auth.RegisterRequest;
import ma.ac.exam.helpdeskapi.web.dto.auth.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me(org.springframework.security.core.Authentication authentication) {
        return authService.getProfile(authentication.getName());
    }
}
