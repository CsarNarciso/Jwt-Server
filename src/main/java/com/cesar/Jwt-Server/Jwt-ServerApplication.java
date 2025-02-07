package com.cesar.Jwt-Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserService userService){
		
		return args -> {
			//Create and pre-load test entities on DB
			
			//PERMISSIONS
			Set<String> permissionNames = Set.of("READ", "WRITE", "DELETE", "REFACTOR");
			List<PermissionEntity> permissions = new ArrayList<>();
			
			//For each permission namen
			permissionsNames.forEach(name -> {
				
				//create new entity
				PermissionEntity permission = PermissionEntity
								.builder()
									.name(name)
								.build();
				permissions.add(permission);
			});
			
			//ROLES
			List<RoleEntity> roles = new ArrayList<>();
			String[][] rolePermissions = {
				{"READ"},
				{"READ", "WRITE"},
				{"READ", "WRITE", "DELETE"},
				{"READ", "WRITE", "DELETE", "REFACTOR"}
			}
			
			//For each role name,
			for(int i = 0; i < RoleEnum.values(); i++){
				
				//create new entity
				RoleEntity role = RoleEntity
							.builder()
								.name(RoleEnum.values().get(i))
								.permissions(rolePermissions[i].asSet())
							.build();
				roles.add(role);
			});
			
			//USERS
			Set<String> usernames = Set.of("juan12", "maria54", "pedroGarcia");
			String[][] userRoles = {
				{RoleEnum.USER, RoleEnum.GUEST},
				{RoleEnum.ADMIN},
				{RoleEnum.DEVELOPER}
			};
			List<UserEntity> users = new ArrayList<>();
			
			//Create PasswordEncoder to encode passwords
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			//For each username
			for(int i = 0; i < usernames.length(); i++){
				
				//create new entity
				UserEntity user = userService.create(usernames[i], passwords[i], userRoles[i].asSet());
				users.add(user);			
			}
			//Save all users (along with their roles and permissions: cascade) on DB
			userService.saveAll(users);
		}
	}
	
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
