package com.auth.auth.infraestructure.adapters.persistence;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public final class UserEntity {
    

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;
}
