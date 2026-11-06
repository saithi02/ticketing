package com.sumpositive.ticketing.user.service;


import com.sumpositive.ticketing.user.dto.UserResponse;
import com.sumpositive.ticketing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public List<UserResponse> getAssignableUsers() {

        return userRepository.findAll().stream()
            .filter(user -> user.isActive())
            .filter(user -> user.getRoles().stream().anyMatch(role ->
                role.getName().equals("ROLE_AGENT") ||
                role.getName().equals("ROLE_ADMIN")
            ))
            .map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles().stream()
                    .map(role -> role.getName())
                    .findFirst()
                    .orElse("ROLE_USER")
            ))
            .toList();
    }
}

