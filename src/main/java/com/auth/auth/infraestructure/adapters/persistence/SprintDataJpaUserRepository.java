package com.auth.auth.infraestructure.adapters.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintDataJpaUserRepository extends JpaRepository<UserEntity, String>{
 
    Optional<UserEntity> findByEmail(String email);

}
