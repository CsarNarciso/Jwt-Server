package com.cesar.JwtServer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.repository.RoleRepository;

@Service
public class RoleService{
	
	public List<RoleEntity> getRoleEntitiesByNames(List<String> roleNames){
		return repo.findRoleEntitiesByNameIn(roleNames);
	}
	
	
	
	public RoleService(RoleRepository repo){
		this.repo = repo;
	}
	
	private final RoleRepository repo;
}