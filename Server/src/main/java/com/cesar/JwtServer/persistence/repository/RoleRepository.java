package com.cesar.JwtServer.persistence.repository;

import java.util.Optional;

import com.cesar.JwtServer.persistence.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesar.JwtServer.persistence.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{

	Optional<RoleEntity> findByName(RoleEnum name);
}