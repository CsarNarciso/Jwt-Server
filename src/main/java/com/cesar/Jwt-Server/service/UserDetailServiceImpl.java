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
	
	public Authentication authenticateByUsernameAndPassword(String username, String password){
		
		UserDetails user = loadUserByUsername(username);
		
		//If user exists (correct credentials)
		if(user!=null && passwordEncoder.matches(user.getPassword(), password)){
			
			//Authenticate user in Security context
			Authentication authentication = new 
						UsernamePasswordAuthenticationToken(username, null, user.getPermissions());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		
			return authentication;
		}
		
		throw new BadCredentialsException("Invalid username or password");
	}
	
	public String login(LogInRequest loginRequest){
		
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		
		//Generate Jwt token if successful authentication
		return jwtUtils.createToken(authenticateByUsernameAndPassword(username, password));
	}
	
	
	public UserDetailServiceImpl(UserRepository userRepo, ModelMapper mapper, JwtUtils jwtUtils){
		this.userRepo = userRepo;
		this.mapper = mapper;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = new PasswordEncoder();
	}
	
	private final UserRepository userRepo;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
}