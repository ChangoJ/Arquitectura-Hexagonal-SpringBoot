package com.auth.auth.infraestructure.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDto(

    @NotBlank
    String username,

    @NotBlank
    String email,
    
    @NotBlank
    String password
){}