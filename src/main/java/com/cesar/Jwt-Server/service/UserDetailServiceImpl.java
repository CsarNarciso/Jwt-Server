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
	
	public String signup(SignUpRequest signupRequest){
		
		//Get user data
		String username = signupRequest.getUsername();
		String password = signupRequest.getPassword();
		
		//Get roles entities (only existing ones on DB) based on request role names
		List<RoleEntity> roles = roleRepo.findRoleEntitiesByNameIn(signupRequest.getRoleNames());
		
		if(roles.isEmpty()){
			throw new IllegalArgumentException("Only avaliable existing role names")
		}
		
		//Create and save new user in DB
		UserEntity userEntity = UserEntity
				.builder()
					.username(username)
					.password(passwordEncoder.encode(password))
					.isEnabled(true)
					.isAccountNoExpired(true)
					.isAccountNoLocked(true)
					.isCredentialNoExpired(true)
					.roles(roles.asSet())
				.build();
		userEntity = userRepo.save(userEntity);
		
		//Try to authenticate
		Authentication authentication = authenticateByUsernameAndPassword(username, password);
		
		//If successful, generate Jwt Token as response
		return jwtUtils.createToken(authentication);
	}
	
	
	
	
	public UserDetailServiceImpl(UserRepository userRepo, RoleRepository roleRepo, ModelMapper mapper, JwtUtils jwtUtils){
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.mapper = mapper;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = new PasswordEncoder();
	}
	
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
}