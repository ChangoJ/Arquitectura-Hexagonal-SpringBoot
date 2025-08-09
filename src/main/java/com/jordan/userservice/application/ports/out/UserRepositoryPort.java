package com.jordan.userservice.application.ports.out;

import java.util.Optional;

import com.jordan.userservice.domain.model.User;

public interface UserRepositoryPort {
    public User saveUser(User user);

    public Optional<User> findUserByEmail(String email);
}
