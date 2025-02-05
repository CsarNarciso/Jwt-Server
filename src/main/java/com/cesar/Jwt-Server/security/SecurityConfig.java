

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
	
	@Bean
	public SecutiryFilterChain securityFilterChain(HttpSecutiry http){
		http
			.csrf(c -> c.disable())
			.httpBasic(Customizer.withDefaults())
			.sessionManagment(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequest(http -> {
				
				//Http endpoints for resource operations
				http.requestMatchers(HttpMethod.PATCH, "/patch").hasAnyAuthority("UPDATE", "REFACTOR");
				
				http.requestMatchers(HttpMethod.DELETE, "/delete").hasRole(RoleEnum.ADMIN);
				
				//For auth operations
				http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
			})
			.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
			.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration){
		return authConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailsServiceImpl){
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	public SecurityConfig(JwtUtils jwtUtils){
		this.jwtUtils = jwtUtils;
	}
	
	private final JwtUtils jwtUtils;
}