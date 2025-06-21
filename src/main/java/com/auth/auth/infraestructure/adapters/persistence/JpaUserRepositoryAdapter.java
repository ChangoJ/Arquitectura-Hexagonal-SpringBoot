package com.auth.auth.infraestructure.adapters.persistence;

import com.auth.auth.domain.model.User;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.auth.auth.application.ports.out.UserRepositoryPort;
import com.auth.auth.common.exceptions.ResourceNotFoundException;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SprintDataJpaUserRepository sprintDataJpaUserRepository;

    public JpaUserRepositoryAdapter(SprintDataJpaUserRepository sprintDataJpaUserRepository) {
        this.sprintDataJpaUserRepository = sprintDataJpaUserRepository;
    }

    @Override
    public User saveUser(User user) {
        String normalizedEmail = user.email().toLowerCase();
      

        UserEntity userEntity = new UserEntity(user.id(), user.username(), normalizedEmail, user.password());

        final UserEntity saveUserEntity = this.sprintDataJpaUserRepository.save(userEntity);

        return new User(saveUserEntity.getId(), saveUserEntity.getUsername(), saveUserEntity.getEmail(),
                saveUserEntity.getPassword());

    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        String normalizedEmail = email.toLowerCase();
        Optional<UserEntity> userEntity = this.sprintDataJpaUserRepository.findByEmail(normalizedEmail);

        return userEntity.map(entity -> new User(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword()));
    }

}
