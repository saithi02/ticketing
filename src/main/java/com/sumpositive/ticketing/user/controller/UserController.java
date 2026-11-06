package com.sumpositive.ticketing.user.controller;

import com.sumpositive.ticketing.user.dto.UserResponse;
import com.sumpositive.ticketing.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    public UserService getUserService() {
		return userService;
	}

	private final UserService userService;

    public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/assignable")
    public List<UserResponse> getAssignableUsers() {
        return userService.getAssignableUsers();
    }
}
