package com.auth.auth.application.ports.out;


import com.auth.auth.domain.model.User;

public interface UserRepositoryPort {
    public User saveUser(User user);  
    
     public User findUserByEmail(String email);  
}
