package com.cesar.JwtServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cesar.JwtServer.persistence.entity.PermissionEntity;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.service.UserService;

@SpringBootApplication
public class JwtServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserService userService) {

		return args -> {
			// Create and pre-load test entities on DB

			// PERMISSIONS
			Set<String> permissionNames = Set.of("READ", "WRITE", "DELETE", "REFACTOR");
			Map<String, PermissionEntity> permissions = new HashMap<>();

			// For each permission namen
			permissionNames.forEach(name -> {

				// create new entity
				PermissionEntity permission = PermissionEntity.builder().name(name).build();
				permissions.put(name, permission);
			});

			// ROLES
			Map<String, RoleEntity> roles = new HashMap<>();
			PermissionEntity[][] rolePermissions = { { permissions.get("READ") },
					{ permissions.get("READ"), permissions.get("WRITE") },
					{ permissions.get("READ"), permissions.get("WRITE"), permissions.get("DELETE") },
					{ permissions.get("READ"), permissions.get("WRITE"), permissions.get("DELETE"),
							permissions.get("REFACTOR") } };

			// For each role name,
			for (int i = 0; i < RoleEnum.values().length; i++) {

				// create new entity
				RoleEntity role = RoleEntity.builder().name(RoleEnum.values()[i])
						.permissions(new HashSet<>(Arrays.asList(rolePermissions[i]))).build();
				roles.put(RoleEnum.values()[i].name(), role);
			}
			;

			// USERS
			String[] usernames = { "juan12", "maria54", "pedroGarcia" };
			RoleEntity[][] userRoles = { { roles.get(RoleEnum.USER.name()), roles.get(RoleEnum.GUEST.name()) },
					{ roles.get(RoleEnum.ADMIN.name()) }, { roles.get(RoleEnum.DEVELOPER.name()) } };
			List<UserEntity> users = new ArrayList<>();

			// For each username
			for (int i = 0; i < usernames.length; i++) {

				// create new entity
				UserEntity user = userService.create(usernames[i], "letmein",
						new HashSet<>(Arrays.asList(userRoles[i])));
				users.add(user);
			}
			// Save all users (along with their roles and permissions: cascade) on DB
			userService.saveAll(users);
		};
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
