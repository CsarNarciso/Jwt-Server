

@EnableWebSecurity
public class SecurityConfig{
	
	@Bean
	public SecutiryFilterChain securityFilterChain(HttpSecutiry http){
		http
			.builder()
				
				.csrf(c -> {
					c.disable();
				})
				.requestMatchers(HttpMethod.GET, "/free").allowAny()
				.requestMatchers(HttpMethod.GET, "/secured").withRoles("DEV")
				.anyRequest().denyAll()
			.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(){
		return new AuthenticationManager(authenticationProvider());
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(){
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetails(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
	}
	
	@Bean
	public UserDetailsService userDetailsService(){
		
		//Create in memory users for testing
		List<UserDetails> users = new ArrayList<>();
		
		users.add(
			UserDetails
				.builder()
				.withUsername("u1")
				.withPassword("1")
				.withRoles("USER")
				.withPermissions({"READ"})
				.build()
		);
		
		users.add(
			UserDetails
				.builder()
				.withUsername("u2")
				.withPassword("2")
				.withRoles("ADMIN")
				.withPermissions({"READ", "WRITE"})
				.build()
		);
		
		users.add(
			UserDetails
				.builder()
				.withUsername("me")
				.withPassword("changeme")
				.withRoles({"TESTER", "DEV"})
				.withPermissions({"WRITE", "READ", "REFACTOR"})
				.build()
		);
		
		return new InMemoryUserDetailsService(users);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new OpPasswordEncoder(); //No encrypted password just for testing!!
	}
}