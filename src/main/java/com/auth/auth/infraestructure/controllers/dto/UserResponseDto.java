package com.auth.auth.infraestructure.controllers.dto;

import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String username,
    String email
){}
