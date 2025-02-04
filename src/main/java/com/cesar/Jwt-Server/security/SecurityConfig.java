

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
				
				http.requestMatchers(HttpMethod.PUT, "/put").;
				
				http.requestMatchers(HttpMethod.PATCH, "/patch").hasAnyAuthority("UPDATE", "REFACTOR");
				
				http.requestMatchers(HttpMethod.DELETE, "/delete").hasRole(RoleEnum.ADMIN);
			})
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
}