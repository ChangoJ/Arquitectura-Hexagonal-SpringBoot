package com.auth.auth.infraestructure.adapters.persistence;

import com.auth.auth.domain.model.User;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.auth.auth.application.ports.out.UserRepositoryPort;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SprintDataJpaUserRepository sprintDataJpaUserRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserRepositoryAdapter(SprintDataJpaUserRepository sprintDataJpaUserRepository, PasswordEncoder passwordEncoder) {
        this.sprintDataJpaUserRepository = sprintDataJpaUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {

        String passwordHashed = this.passwordEncoder.encode(user.email());        

        UserEntity userEntity = new UserEntity(user.id(), user.username(), user.email(), passwordHashed);

        if (this.sprintDataJpaUserRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new IllegalStateException("El email ya está registrado");
        }       

        final UserEntity saveUserEntity = this.sprintDataJpaUserRepository.save(userEntity);

        return new User(saveUserEntity.getId(), saveUserEntity.getUsername(), saveUserEntity.getEmail(),
                saveUserEntity.getPassword());

    }

    @Override
    public Optional<User> findUserByEmail(String email) {

        final Optional<UserEntity> userFound = this.sprintDataJpaUserRepository.findByEmail(email);

        return userFound.map(userEntity -> new User(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getPassword()));

    }

}
