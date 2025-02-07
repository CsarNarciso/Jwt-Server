package com.cesar.Jwt-Server.service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserEntity> userOptional = userService.getByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException); 
		
		//If user exists
		UserEntity entity = userOptional.get();
		
		//Map entity and authorities to Spring Secutiry User (from UserDetails)
		User user = mapper.map(entity, User.class);
		
		//Extract their roles and permissions as Spring Security authorities
		user.setAuthorities(getAuthoritiesFromRoles(entity.getRoles()));
		
		return user;
	}
	
	public void getAuthoritiesFromRoles(Set<RoleEntity> roles){
		
		Set<SimpleGrandAuthority> authorities = new ArrayList<>();

		//Roles
		roles
			.forEach(role -> authorities.add(new SimpleGrandAuthority("ROLE_".concat(role.name()))));
			
		//Permissions
		roles.stream()
				.flatMap(role -> role.getPermissions().stream())
				.forEach(permission -> authorities.add(new SimpleGrandAuthority(permission.getName())));
				
		return authorities;
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
		Set<RoleEntity> roles = roleService.getRoleEntitiesByNames(signupRequest.getRoleNames()).asSet();
		
		if(roles.isEmpty()){
			throw new IllegalArgumentException("Only avaliable existing role names")
		}
		
		//Create and save new user in DB
		userService.create(username, password, roles);
		
		//Try to authenticate
		Authentication authentication = authenticateByUsernameAndPassword(username, password);
		
		//If successful, generate Jwt Token as response
		return jwtUtils.createToken(authentication);
	}
	
	
	
	
	public UserDetailServiceImpl(UserService userService, RoleService roleService, ModelMapper mapper, JwtUtils jwtUtils){
		this.userService = userService;
		this.roleService = roleService;
		this.mapper = mapper;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = new PasswordEncoder();
	}
	
	
	private final UserService userService;
	private final RoleService roleService;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
}