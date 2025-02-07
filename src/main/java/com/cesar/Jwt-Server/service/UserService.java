package com.cesar.Jwt-Server.service;

@Service
public class UserService{
	
	public UserEntity create(String username, String password, Set<RoleEntity> roles){
		return repo.save(
			UserEntity
				.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.isEnabled(true)
				.isAccountNoExpired(true)
				.isAccountNoLocked(true)
				.isCredentialNoExpired(true)
				.roles(roles)
				.build()
		);
	}
	
	public Optional<UserEntity> getByUsername(String username){
		return repo.findByUsername(username);
	}
	
	
	public UserService(UserRepository repo){
		this.repo = repo;
		this.passwordEncoder = new PasswordEncoder();
	}
	
	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;
}