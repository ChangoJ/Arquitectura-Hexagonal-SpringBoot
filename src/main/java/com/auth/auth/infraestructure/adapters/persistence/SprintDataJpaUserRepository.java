package com.auth.auth.infraestructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintDataJpaUserRepository extends JpaRepository<UserEntity, String>{
 
    UserEntity findByEmail(String email);

}
