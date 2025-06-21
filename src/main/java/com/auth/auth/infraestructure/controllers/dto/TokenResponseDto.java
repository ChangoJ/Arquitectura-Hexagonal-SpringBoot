package com.auth.auth.infraestructure.controllers.dto;


public record TokenResponseDto(
        String accessToken,
        String refreshToken) {
}