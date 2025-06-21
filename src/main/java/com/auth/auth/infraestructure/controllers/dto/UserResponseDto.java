package com.auth.auth.infraestructure.controllers.dto;



public record UserResponseDto(
    String id,
    String username,
    String email
){}
