package com.sumpositive.ticketing.auth.controller;

import com.sumpositive.ticketing.auth.dto.*;
import com.sumpositive.ticketing.auth.service.AuthService;
import com.sumpositive.ticketing.user.repository.UserRepository;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public AuthController(AuthService authService, UserRepository userRepository) {
		super();
		this.authService = authService;
		this.userRepository = userRepository;
	}

	private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public AuthResponse register(
        @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
        @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public Object currentUser(Authentication authentication) {

        return userRepository.findByUsername(
            authentication.getName()
        ).map(user -> java.util.Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "fullName", user.getFullName(),
            "roles", user.getRoles().stream()
                .map(role -> role.getName())
                .toList()
        )).orElseThrow();
    }
}
