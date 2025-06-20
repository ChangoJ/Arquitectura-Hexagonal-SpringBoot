package com.auth.auth.infraestructure.controllers.dto;

public record UserLoginResponseDto(
    String username,
    String email
){}
