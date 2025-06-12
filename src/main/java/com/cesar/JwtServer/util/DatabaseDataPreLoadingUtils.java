package com.cesar.JwtServer.util;

import com.cesar.JwtServer.persistence.entity.PermissionEntity;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.RoleRepository;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseDataPreLoadingUtils {

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

    public void initTestUsers() {
        // Pre-load test users
        UserEntity user = buildUser("user", Set.of(roles.get(RoleEnum.USER)));
        UserEntity admin = buildUser("admin", Set.of(roles.get(RoleEnum.ADMIN)));
        userRepo.saveAll(List.of(user, admin));
    }



    private PermissionEntity buildPermission(String name) {
        return PermissionEntity.builder().name(name).build();
    }

    private void buildRole(RoleEnum name, Set<PermissionEntity> permissions) {
        roles.put(name, RoleEntity.builder().name(name).permissions(permissions).build());
    }

    private UserEntity buildUser(String username, Set<RoleEntity> roles) {
        return UserEntity
                .builder()
                .username(username)
                .password(passwordEncoder.encode("letmein"))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roles)
                .build();
    }



    public DatabaseDataPreLoadingUtils(BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepo, UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private Map<RoleEnum, RoleEntity> roles;
}