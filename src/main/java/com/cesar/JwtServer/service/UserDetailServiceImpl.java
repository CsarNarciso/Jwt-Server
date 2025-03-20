package com.cesar.JwtServer.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.cesar.JwtServer.persistence.repository.RoleRepository;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;
import com.cesar.JwtServer.presentation.dto.SignUpResponse;
import com.cesar.JwtServer.util.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity entity = userRepo.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException(username)); 
		
		//If user exists
		
		//Convert entity and authorities to Spring Security objects (UserDetails and GrantedAuthorities)

        return new User(
                entity.getUsername(),
                entity.getPassword(),
                entity.isEnabled(),
                entity.isAccountNoExpired(),
                entity.isCredentialNoExpired(),
                entity.isAccountNoLocked(),
                getAuthoritiesFromRoles(entity.getRoles())
        );
	}
	
	public Set<SimpleGrantedAuthority> getAuthoritiesFromRoles(Set<RoleEntity> roles){
		
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		//Roles
		roles
			.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName().name()))));
			
		//Permissions
		roles.stream()
				.flatMap(role -> role.getPermissions().stream())
				.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
				
		return authorities;
	}
	
	public Authentication authenticateByUsernameAndPassword(String username, String password){
		
		UserDetails user = loadUserByUsername(username);
		
		//If user exists (correct credentials)
		if(user!=null && passwordEncoder.matches(user.getPassword(), password)){
			
			//Authenticate user in Security context
			Authentication authentication = new 
						UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		
			return authentication;
		}
		
		throw new BadCredentialsException("Invalid username or password");
	}
	
	public String login(LogInRequest loginRequest){
		
		String username = loginRequest.username();
		String password = loginRequest.password();
		
		//Generate Jwt token if successful authentication
		return jwtUtils.createToken(authenticateByUsernameAndPassword(username, password));
	}
	
	public SignUpResponse signup(SignUpRequest signupRequest){
		
		//Get user data
		String username = signupRequest.username();
		String password = signupRequest.password();
		
		//Get roles entities (only existing ones on DB) based on request role names
		Set<RoleEntity> roles = new HashSet<>(roleRepo.findRoleEntitiesByNameIn(signupRequest.roleNames()));
		
		if(roles.isEmpty()){
			throw new IllegalArgumentException("Only available existing role names");
		}
		
		//Create and save new user in DB
		UserEntity user = UserEntity.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.isEnabled(true)
				.accountNoExpired(true)
				.accountNoLocked(true)
				.credentialNoExpired(true)
				.roles(roles)
			.build();

		userRepo.save(user);
		
		return SignUpResponse
				.builder()
					.username(username)
					.created(true)
				.build();
	}
	
	
	
	
	public UserDetailServiceImpl(UserRepository userRepo, RoleRepository roleRepo, ModelMapper mapper, JwtUtils jwtUtils){
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.mapper = mapper;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final JwtUtils jwtUtils;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ModelMapper mapper;
}