package com.jordan.userservice.application.ports.in;

import java.util.Optional;

import com.jordan.userservice.domain.model.User;

public interface GetUserUseCase {
    Optional<User> getUser(String email);
}
