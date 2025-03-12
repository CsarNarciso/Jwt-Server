package com.cesar.JwtServer.service;

import java.util.*;
import java.util.stream.Collectors;

import com.cesar.JwtServer.persistence.entity.PermissionEntity;
import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.persistence.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.repository.RoleRepository;

@Service
public class RoleService{


	public List<RoleEntity> getRoleEntitiesByNames(List<String> roleNames){
		return repo.findRoleEntitiesByNameIn(roleNames);
	}


	
	public RoleService(RoleRepository repo, PermissionRepository permissionRepo) {
		this.repo = repo;
		this.permissionRepo = permissionRepo;
	}
	
	private final RoleRepository repo;

	private final PermissionRepository permissionRepo;
}