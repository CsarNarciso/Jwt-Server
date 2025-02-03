

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