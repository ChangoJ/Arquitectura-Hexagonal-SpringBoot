package com.auth.auth.application.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.application.ports.in.CreateUserUseCase;
import com.auth.auth.application.ports.in.GetUserUseCase;
import com.auth.auth.application.ports.out.UserRepositoryPort;
import com.auth.auth.common.exceptions.ResourceAlreadyExistsException;
import com.auth.auth.common.exceptions.ResourceNotFoundException;
import com.auth.auth.domain.model.User;

@Service
public class UserService implements CreateUserUseCase, GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {

        if (userRepositoryPort.findUserByEmail(user.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with email " + user.email() + " already exists.");
        }

        String encodedPassword = passwordEncoder.encode(user.password());
        User userToSave = new User(user.id(), user.username(), user.email(), encodedPassword);
        return userRepositoryPort.saveUser(userToSave);
    }

    @Override
    public Optional<User> getUser(String email) {
        Optional<User> user = this.userRepositoryPort.findUserByEmail(email);
        return user;
    }

}
