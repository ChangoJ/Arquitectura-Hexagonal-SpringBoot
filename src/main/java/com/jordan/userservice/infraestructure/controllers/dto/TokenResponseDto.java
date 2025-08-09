package com.jordan.userservice.infraestructure.controllers.dto;

public record TokenResponseDto(
                String accessToken,
                String refreshToken) {
}