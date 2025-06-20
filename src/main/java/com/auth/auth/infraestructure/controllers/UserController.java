package com.auth.auth.infraestructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.application.ports.in.AuthUserUseCase;
import com.auth.auth.application.ports.in.CreateUserUseCase;
import com.auth.auth.domain.model.User;
import com.auth.auth.infraestructure.controllers.dto.UserRegisterRequestDto;
import com.auth.auth.infraestructure.controllers.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Microservice", description = "This is the User Microservice")
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;



    public UserController(CreateUserUseCase createUserUseCase, AuthUserUseCase authUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }


    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User created")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("register")
    public UserResponseDto createUser(@Valid @RequestBody UserRegisterRequestDto userRequestDto) {
        final User user = new User(null, userRequestDto.username(),userRequestDto.email(), userRequestDto.password());

        User userCreated = this.createUserUseCase.createUser(user);

        return new UserResponseDto(userCreated.id(),userCreated.email(), userCreated.username());

    }

 
    



}
