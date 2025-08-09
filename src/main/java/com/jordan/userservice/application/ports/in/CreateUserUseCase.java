package com.jordan.userservice.application.ports.in;

import com.jordan.userservice.domain.model.User;

public interface CreateUserUseCase {
    public User createUser(User user);
}
