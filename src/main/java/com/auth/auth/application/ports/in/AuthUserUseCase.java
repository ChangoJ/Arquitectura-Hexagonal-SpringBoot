package com.auth.auth.application.ports.in;

import java.util.Optional;

import com.auth.auth.domain.model.User;

public interface AuthUserUseCase {
    public Optional<User> loginUser(String email);
}
