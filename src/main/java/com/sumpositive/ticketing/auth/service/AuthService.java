package com.sumpositive.ticketing.auth.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumpositive.ticketing.auth.dto.AuthResponse;
import com.sumpositive.ticketing.auth.dto.LoginRequest;
import com.sumpositive.ticketing.auth.dto.RegisterRequest;
import com.sumpositive.ticketing.security.JwtService;
import com.sumpositive.ticketing.user.entity.Role;
import com.sumpositive.ticketing.user.entity.User;
import com.sumpositive.ticketing.user.repository.RoleRepository;
import com.sumpositive.ticketing.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException(
                "Username already exists"
            );
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                "Email already exists"
            );
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() ->
                new IllegalStateException("Default role not configured"));

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.getRoles().add(userRole);

        userRepository.save(user);

        return createAuthResponse(user);
    }
    
    // ADMIN CREATES ADMIN, AGENT OR USER
    @Transactional
    public AuthResponse createUser(
            RegisterRequest request,
            String roleName) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!Set.of(
                "ROLE_ADMIN",
                "ROLE_AGENT",
                "ROLE_USER"
        ).contains(roleName)) {

            throw new IllegalArgumentException("Invalid role");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                    new IllegalStateException("Role not configured"));

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());

        // Automatic BCrypt encryption
        user.setPassword(
            passwordEncoder.encode(request.password())
        );

        user.setActive(true);
        user.getRoles().add(role);

        userRepository.save(user);

        return createAuthResponse(user);
    }

    
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        User user = userRepository.findByUsername(request.username())
            .orElseThrow();

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {

        UserDetails details =
            org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                    user.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new)
                )
                .build();

        String token = jwtService.generateToken(details);

        Set<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        return new AuthResponse(
            token,
            user.getUsername(),
            roles
        );
    }
}
