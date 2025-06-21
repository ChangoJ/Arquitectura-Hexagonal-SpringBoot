package com.auth.auth.application.ports.in;


import com.auth.auth.domain.model.User;

public interface GetUserUseCase {
    User getUser(String email);
}
