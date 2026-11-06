
package com.sumpositive.ticketing.config;

import com.sumpositive.ticketing.user.entity.User;
import com.sumpositive.ticketing.user.entity.Role;
import com.sumpositive.ticketing.user.repository.UserRepository;
import com.sumpositive.ticketing.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class Admin {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
    CommandLineRunner createDefaultAdmin() {

        return args -> {

            String username = "admin1";
            String rawPassword = "admin123";

            // Check whether admin already exists
            User admin = userRepository
                    .findByUsername(username)
                    .orElse(null);

            if (admin != null) {

                // Update existing admin password
                admin.setPassword(
                    passwordEncoder.encode(rawPassword)
                );

                userRepository.save(admin);

                System.out.println(
                    "Existing admin password updated using BCrypt."
                );

                return;
            }

            // Get ADMIN role
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() ->
                        new RuntimeException("ADMIN role not found"));

            // Create new admin
            admin = new User();

            admin.setUsername(username);
            admin.setPassword(
                passwordEncoder.encode(rawPassword)
            );
            admin.setFullName("System Administrator1");
            admin.setEmail("admin@gmail.com");
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            System.out.println(
                "Default admin created successfully using BCrypt."
            );
        };
    }
}
