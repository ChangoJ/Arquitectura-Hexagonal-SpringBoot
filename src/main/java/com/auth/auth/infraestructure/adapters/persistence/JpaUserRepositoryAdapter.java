package com.auth.auth.infraestructure.adapters.persistence;

import com.auth.auth.domain.model.User;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.auth.auth.application.ports.out.UserRepositoryPort;
import com.auth.auth.common.exceptions.ResourceNotFoundException;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SprintDataJpaUserRepository sprintDataJpaUserRepository;
    // private final PasswordEncoder passwordEncoder;

    public JpaUserRepositoryAdapter(SprintDataJpaUserRepository sprintDataJpaUserRepository) {
        this.sprintDataJpaUserRepository = sprintDataJpaUserRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {

        UserEntity userEntity = new UserEntity(user.id(), user.username(), user.email(), user.password());

        this.findUserByEmail(user.email());

        final UserEntity saveUserEntity = this.sprintDataJpaUserRepository.save(userEntity);

        return new User(saveUserEntity.getId(), saveUserEntity.getUsername(), saveUserEntity.getEmail(),
                saveUserEntity.getPassword());

    }

    @Override
    public User findUserByEmail(String email) {

        final UserEntity userFound = this.sprintDataJpaUserRepository.findByEmail(email);

        if (userFound == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        return new User(userFound.getId(), userFound.getUsername(), userFound.getEmail(), userFound.getPassword());

    }

}
