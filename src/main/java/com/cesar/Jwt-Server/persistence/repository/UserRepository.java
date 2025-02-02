package com.cesar.Jwt-Server.persistence.repository;

@Repository
public interface UserRepository() extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);
}