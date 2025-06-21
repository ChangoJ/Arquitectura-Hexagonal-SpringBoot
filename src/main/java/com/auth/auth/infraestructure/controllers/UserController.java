package com.auth.auth.infraestructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.application.ports.in.CreateUserUseCase;
import com.auth.auth.application.ports.in.GetUserUseCase;
import com.auth.auth.common.exceptions.UnauthorizedException;
import com.auth.auth.config.JwtUtil;
import com.auth.auth.domain.model.User;
import com.auth.auth.infraestructure.controllers.dto.LoginRequestDto;
import com.auth.auth.infraestructure.controllers.dto.RefreshTokenRequestDto;
import com.auth.auth.infraestructure.controllers.dto.TokenResponseDto;
import com.auth.auth.infraestructure.controllers.dto.UserRegisterRequestDto;
import com.auth.auth.infraestructure.controllers.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "User Microservice", description = "This is the User Microservice")
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase,
            AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Create a new user", description = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User created")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("register")
    public UserResponseDto createUser(@Valid @RequestBody UserRegisterRequestDto userRequestDto) {
        final User user = new User(null, userRequestDto.username(), userRequestDto.email(), userRequestDto.password());

        User userCreated = this.createUserUseCase.createUser(user);

        return new UserResponseDto(userCreated.id(), userCreated.username(), userCreated.email());

    }

    @GetMapping("/{email}")
    public UserResponseDto getUser(@Email @PathVariable String email) {

        Optional<User> userOptional = this.getUserUseCase.getUser(email);

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserResponseDto(user.id(), user.username(), user.email());

    }

    @Operation(summary = "Login user", description = "Authenticate user and return JWT tokens")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid credentials " + e);
        } catch (InternalAuthenticationServiceException e) {
            throw new UnauthorizedException("Invalid credentials" + e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.email());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken));
    }

    @Operation(summary = "Refresh JWT token", description = "Generate new access token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed")
    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.refreshToken();
        String userEmail = jwtUtil.getUsernameFromToken(refreshToken);

        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                return ResponseEntity.ok(new TokenResponseDto(newAccessToken, refreshToken));
            }
        }

        throw new RuntimeException("Invalid refresh token");
    }

}
