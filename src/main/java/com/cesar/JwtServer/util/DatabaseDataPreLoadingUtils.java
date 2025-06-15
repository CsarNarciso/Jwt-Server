package com.cesar.JwtServer.util;

import com.cesar.JwtServer.persistence.entity.*;
import com.cesar.JwtServer.persistence.repository.RoleRepository;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.cesar.JwtServer.persistence.entity.PermissionEnum.*;
import static com.cesar.JwtServer.persistence.entity.RoleEnum.*;

@Component
public class DatabaseDataPreLoadingUtils {

    @Transactional
    public void initDefaultPermissionsAndRoles() {

        // Pre-load default application' permissions and roles entities on DB

        // PERMISSIONS
        PermissionEntity read = buildPermission(READ);
        PermissionEntity write = buildPermission(WRITE);
        PermissionEntity delete = buildPermission(DELETE);
        PermissionEntity refactor = buildPermission(REFACTOR);

        // ROLES
        buildRole(USER, Set.of(read, write));
        buildRole(ADMIN, Set.of(read, write, delete, refactor));

        //Save all roles (along with their permissions: cascade ALL) on DB
        roleRepo.saveAll(roles.values());
    }

    @Transactional
    public void initTestUser() {
        // Preload test user
        UserEntity user = buildUser("user", "password", Set.of(roles.get(RoleEnum.USER)));
        userRepo.save(user);
    }

    @Transactional
    public void initAdmins() {
        // Preload default admins
        UserEntity admin = buildUser("admin", "letmein", Set.of(roles.get(RoleEnum.ADMIN)));
        userRepo.save(admin);
    }



    private PermissionEntity buildPermission(PermissionEnum name) {
        return PermissionEntity.builder().name(name).build();
    }

    private void buildRole(RoleEnum name, Set<PermissionEntity> permissions) {
        roles.put(name, RoleEntity.builder().name(name).permissions(permissions).build());
    }

    private UserEntity buildUser(String username, String password, Set<RoleEntity> r) {
        return UserEntity
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(r)
                .build();
    }



    public DatabaseDataPreLoadingUtils(RoleRepository roleRepo, UserRepository userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        passwordEncoder = new BCryptPasswordEncoder();
        roles = new HashMap<>();
    }
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<RoleEnum, RoleEntity> roles;
}