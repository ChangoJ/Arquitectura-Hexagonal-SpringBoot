package com.auth.auth.infraestructure.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
    @NotBlank
    String email,

    @NotBlank
    String password
){}
