package com.auth.auth.infraestructure.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
        @NotBlank 
        String refreshToken) {
}