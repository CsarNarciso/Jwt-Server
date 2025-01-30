

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Bean
	public SecutiryFilterChain securityFilterChain(HttpSecutiry http){
		http
			.csrf(c -> c.disable())
			.httpBasic(Customizer.withDefaults())
			.sessionManagment(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequest(http -> {
				
				http.requestMatchers(HttpMethod.GET, "/free").permitAll();
				
				http.requestMatchers(HttpMethod.GET, "/secured").withRoles("DEV");
				
				http.anyRequest().denyAll();
			})
			.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration){
		return authConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(){
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public UserDetailsService userDetailsService(){
		
		//Create in memory users for testing
		List<UserDetails> users = new ArrayList<>();
		
		users.add(
			User
				.withUsername("u1")
				.password("1")
				.roles("USER")
				.permissions("READ")
			.build()
		);
		
		users.add(
			User
				.withUsername("u2")
				.password("2")
				.roles("ADMIN")
				.permissions("READ", "WRITE")
			.build()
		);
		
		users.add(
			User
				.withUsername("me")
				.password("changeme")
				.roles("TESTER", "DEV")
				.authorities("WRITE", "READ", "REFACTOR")
			.build()
		);
		
		return new InMemoryUserDetailsManager(users);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return NoOpPasswordEncoder.getInstance(); //No encrypted password just for testing!!
	}
}