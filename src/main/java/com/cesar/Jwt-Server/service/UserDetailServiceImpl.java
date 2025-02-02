package com.cesar.Jwt-Server.service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserEntity> userOptional = userRepo.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException); 
		
		//If user exists
		UserEntity entity = userOptional.get();
		
		//Extract their roles and roles' permissions as Spring Security authorities
		Set<SimpleGrandAuthority> authorities = new ArrayList<>();

		//Roles
		entity.getRoles()
			.forEach(role -> authorities.add(new SimpleGrandAuthority("ROLE_".concat(role.name()))));
			
		//Permissions
		entity.getRoles().stream()
				.flatMap(role -> role.getPermissions().stream())
				.forEach(permission -> authorities.add(new SimpleGrandAuthority(permission.getName())));
		
		//Map entity and authorities to Spring Secutiry User (from UserDetails)
		User user = mapper.map(entity, User.class);
		user.setAuthorities(authorities);
		
		return user;
	}
	
	
	public UserDetailServiceImpl(UserRepository userRepo, ModelMapper mapper){
		this.userRepo = userRepo;
		this.mapper = mapper;
	}
	
	private final UserRepository userRepo;
	private final ModelMapper mapper;
}