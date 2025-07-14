package com.cesar.JwtServer.util;

import com.cesar.JwtServer.persistence.entity.*;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class UserUtils {

    private final AuthorityUtils authorityUtils;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserUtils(AuthorityUtils authorityUtils, UserRepository userRepo) {
        this.authorityUtils = authorityUtils;
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public void initTestUser() {
        // Only use persisted role from roles map
        RoleEntity userRole = authorityUtils.getUserRole();
        userRepo.save(buildUser("user", "password", Set.of(userRole)));
    }

    @Transactional
    public void initDefaultAdmins() {
        RoleEntity adminRole = authorityUtils.getAdminRole();
        userRepo.save(buildUser("admin", "letmein", Set.of(adminRole)));
    }

    public UserEntity buildUser(String username, String password, Set<RoleEntity> roles) {
        return UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();
    }
}