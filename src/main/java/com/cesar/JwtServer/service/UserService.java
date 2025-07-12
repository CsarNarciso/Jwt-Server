package com.cesar.JwtServer.service;

import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity loadByUsername(String username) {
        System.out.println("USERNAME -> " + username);
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}