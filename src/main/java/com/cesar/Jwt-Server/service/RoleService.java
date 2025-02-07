package com.cesar.Jwt-Server.service;

@Service
public class RoleService{
	
	public List<RoleEntity> getRoleEntitiesByNames(){
		return repo.findRoleEntitiesByNameIn(List<String> roleNames);
	}
	
	
	
	public UserService(RoleRepository repo){
		this.repo = repo;
	}
	
	private final RoleRepository repo;
}