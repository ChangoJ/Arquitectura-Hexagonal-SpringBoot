package com.jordan.userservice.infraestructure.adapters.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jordan.userservice.infraestructure.adapters.entities.UserEntity;

@Repository
public interface SprintDataJpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

}
