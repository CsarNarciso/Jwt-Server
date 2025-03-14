package com.cesar.JwtServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cesar.JwtServer.persistence.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cesar.JwtServer.persistence.entity.PermissionEntity;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class JwtServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserRepository userRepo, final BCryptPasswordEncoder passwordEncoder) {

		return args -> {
			// Create and preload test entities on DB

			// PERMISSIONS
			Set<String> permissionNames = Set.of("READ", "WRITE", "DELETE", "REFACTOR");
			Map<String, PermissionEntity> permissions = new HashMap<>();

			// For each permission name
			permissionNames.forEach(name -> {

				// create new entity
				PermissionEntity permission = PermissionEntity.builder().name(name).build();
				permissions.put(name, permission);
			});

			// ROLES
			Map<String, RoleEntity> roles = new HashMap<>();
			String[][] rolePermissionNames = {
					{ "READ" },
					{ "READ", "WRITE" },
					{ "READ", "WRITE", "DELETE" },
					{ "READ", "WRITE", "DELETE", "REFACTOR" }
			};

			// For each role name,
			for (int i = 0; i < RoleEnum.values().length; i++) {

				//Get new role permission entities and create role
				RoleEntity role = RoleEntity.builder()
						.name(RoleEnum.values()[i])
						.permissions(
								Arrays.stream(rolePermissionNames[i])
										.map(permissions::get)
								.collect(Collectors.toSet()))
						.build();

				roles.put(RoleEnum.values()[i].name(), role);
			}

			// USERS
			String[] usernames = { "juan12", "maria54", "pedroGarcia" };
			RoleEntity[][] userRoles = { { roles.get(RoleEnum.USER.name()), roles.get(RoleEnum.GUEST.name()) },
					{ roles.get(RoleEnum.ADMIN.name()) }, { roles.get(RoleEnum.DEVELOPER.name()) } };
			List<UserEntity> users = new ArrayList<>();

			// For each username
			for (int i = 0; i < usernames.length; i++) {

				// create new entity
				UserEntity user = UserEntity.builder()
						.username(usernames[i])
						.password(passwordEncoder.encode("letmein"))
						.isEnabled(true)
						.accountNoExpired(true)
						.accountNoLocked(true)
						.credentialNoExpired(true)
						.roles(new HashSet<>(Arrays.asList(userRoles[i])))
						.build();
				users.add(user);
			}
			// Save all users (along with their roles and permissions: cascade) on DB
			userRepo.saveAll(users);
		};
	}


	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
