package com.sumpositive.ticketing.user.dto;


public record UserResponse(
    Long id,
    String username,
    String email,
    String fullName,
    String role
) {}
