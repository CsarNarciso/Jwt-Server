package com.cesar.JwtServer.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesar.JwtServer.persistence.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
	
	List<RoleEntity> findRoleEntitiesByNameIn(List<String> roleNames);
}