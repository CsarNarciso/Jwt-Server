package com.cesar.JwtServer.security;

import com.cesar.JwtServer.persistence.entity.PermissionEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.cesar.JwtServer.persistence.entity.RoleEnum;
import com.cesar.JwtServer.security.filter.JwtTokenValidator;
import com.cesar.JwtServer.service.UserDetailServiceImpl;
import com.cesar.JwtServer.util.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(c -> c.disable()).httpBasic(Customizer.withDefaults())
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(request -> {

					// Resource operations
					request.requestMatchers(HttpMethod.GET, "/resource/get").hasAuthority(PermissionEnum.READ.name());

					request.requestMatchers(HttpMethod.POST, "/resource/post").hasAuthority(PermissionEnum.WRITE.name());

					request.requestMatchers(HttpMethod.PUT, "/resource/put").hasRole(RoleEnum.ADMIN.name());

					request.requestMatchers(HttpMethod.PATCH, "/resource/patch").hasRole(RoleEnum.ADMIN.name());

					request.requestMatchers(HttpMethod.DELETE, "/resource/delete").hasRole(RoleEnum.ADMIN.name());

					// For auth operations
					request.anyRequest().permitAll();

				}).addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailsServiceImpl) {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public SecurityConfig(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	private final JwtUtils jwtUtils;
}