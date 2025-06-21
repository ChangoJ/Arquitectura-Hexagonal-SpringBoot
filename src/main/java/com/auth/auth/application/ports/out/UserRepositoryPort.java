package com.auth.auth.application.ports.out;


import java.util.Optional;

import com.auth.auth.domain.model.User;

public interface UserRepositoryPort {
    public User saveUser(User user);  
    
     public  Optional<User> findUserByEmail(String email);  
}
