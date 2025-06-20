package com.auth.auth.domain.model;

import java.util.UUID;

public record User(

        UUID id,
        String username,
        String email,
        String passwrod) {
}
