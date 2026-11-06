
package com.sumpositive.ticketing.user.controller;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sumpositive.ticketing.auth.dto.AuthResponse;
import com.sumpositive.ticketing.auth.dto.RegisterRequest;
import com.sumpositive.ticketing.auth.service.AuthService;
import com.sumpositive.ticketing.user.entity.User;
import com.sumpositive.ticketing.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;
    private final UserRepository userRepository;
    
    public AdminUserController(AuthService authService, UserRepository userRepository) {
		super();
		this.authService = authService;
		this.userRepository = userRepository;
	}

	
   
    
	@PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse createUser(
            @Valid @RequestBody RegisterRequest request,
            @RequestParam String role) {

        return authService.createUser(request, role);
    }

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long id) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() ->
	                new IllegalArgumentException("User not found")
	            );

	    // Prevent deleting admin accounts
	    boolean isAdmin = user.getRoles().stream()
	            .anyMatch(role ->
	                role.getName().equals("ROLE_ADMIN")
	            );

	    if (isAdmin) {
	        throw new IllegalArgumentException(
	            "Cannot delete administrator"
	        );
	    }

	    userRepository.delete(user);
	}
	
}
