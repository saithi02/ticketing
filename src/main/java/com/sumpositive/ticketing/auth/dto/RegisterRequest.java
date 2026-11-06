package com.sumpositive.ticketing.auth.dto;


import jakarta.validation.constraints.*;

public record RegisterRequest(

    @NotBlank
    @Size(min = 3, max = 100)
    String username,

    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 8, max = 72)
    String password,

    @NotBlank
    String fullName

) {}
