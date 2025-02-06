package com.cesar.Jwt-Server.persistence.repository;

@Repository
public interface RoleRepository extends JPARepository<RoleEntity, Long>{
	
	List<RoleEntity> findRoleEntitiesByNameIn(List<String> roleNames);
}