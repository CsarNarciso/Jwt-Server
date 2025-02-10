package com.cesar.JwtServer.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.UserRepository;

@Service
public class UserService {
	
	public UserEntity create(String username, String password, Set<RoleEntity> roles){
		return repo.save(
			UserEntity
				.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.isEnabled(true)
				.accountNoExpired(true)
				.accountNoLocked(true)
				.credentialNoExpired(true)
				.roles(roles)
				.build()
		);
	}
	
	public UserEntity save(UserEntity entity){
		return repo.save(entity);
	}
	
	public void saveAll(List<UserEntity> entities){
		repo.saveAll(entities);
	}
	
	public UserEntity createAndSave(String username, String password, Set<RoleEntity> roles){
		return save(create(username, password, roles));
	}
	
	public Optional<UserEntity> getByUsername(String username){
		return repo.findByUsername(username);
	}
	
	
	public UserService(UserRepository repo){
		this.repo = repo;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	private final UserRepository repo;
	private final BCryptPasswordEncoder passwordEncoder;
}