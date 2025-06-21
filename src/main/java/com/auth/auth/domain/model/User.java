package com.auth.auth.domain.model;


public record User(

        String id,
        String username,
        String email,
        String password) {
}
