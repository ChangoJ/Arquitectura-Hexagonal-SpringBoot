package com.auth.auth.application.services;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth.auth.application.ports.in.AuthUserUseCase;
import com.auth.auth.application.ports.in.CreateUserUseCase;
import com.auth.auth.application.ports.out.UserRepositoryPort;
import com.auth.auth.domain.model.User;

import jakarta.validation.Valid;

@Service
public class UserService implements CreateUserUseCase, AuthUserUseCase{

    private final UserRepositoryPort userRepositoryPort;


    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User createUser(User user){
        return this.userRepositoryPort.saveUser(user);
    }

    @Override
    public Optional<User> loginUser(String email){

        return this.userRepositoryPort.findUserByEmail(email);
    }

}
