package com.cesar.JwtServer.util;

import com.cesar.JwtServer.persistence.entity.PermissionEntity;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.RoleRepository;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DatabaseDataPreLoadingUtils {

    @Transactional
    public void initDefaultPermissionsAndRoles() {

        // Pre-load default application' permissions and roles entities on DB

        // PERMISSIONS
        PermissionEntity read = buildPermission("READ");
        PermissionEntity write = buildPermission("WRITE");
        PermissionEntity delete = buildPermission("DELETE");
        PermissionEntity refactor = buildPermission("REFACTOR");

        // ROLES
        buildRole(RoleEnum.GUEST, Set.of(read));
        buildRole(RoleEnum.USER, Set.of(read, write));
        buildRole(RoleEnum.ADMIN, Set.of(read, write, delete));
        buildRole(RoleEnum.DEVELOPER, Set.of(read, write, delete, refactor));

        //Save all roles (along with their permissions: cascade ALL) on DB
        roleRepo.saveAll(roles.values());
    }

    @Transactional
    public void initTestUsers() {
        // Pre-load test users
        RoleEntity role = roleRepo.findById(roles.get(RoleEnum.USER).getId()).orElse(null);
        UserEntity user = buildUser("user", Set.of(role));
        //UserEntity admin = buildUser("admin", Set.of(roles.get(RoleEnum.ADMIN)));
        userRepo.saveAll(List.of(user));
    }



    private PermissionEntity buildPermission(String name) {
        return PermissionEntity.builder().name(name).build();
    }

    private void buildRole(RoleEnum name, Set<PermissionEntity> permissions) {
        roles.put(name, RoleEntity.builder().name(name).permissions(permissions).build());
    }

    private UserEntity buildUser(String username, Set<RoleEntity> r) {
        return UserEntity
                .builder()
                .username(username)
                .password(passwordEncoder.encode("letmein"))
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
    private Map<RoleEnum, RoleEntity> roles;
}