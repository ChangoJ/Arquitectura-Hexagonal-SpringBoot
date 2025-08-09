package com.jordan.userservice.infraestructure.controllers.dto;

public record UserResponseDto(
        String id,
        String username,
        String email) {
}
