package com.cesar.JwtServer.service;

import java.util.HashSet;
import java.util.Set;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.UserEntity;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity foundUser = userRepo.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException(username)); 

		//Convert entity and authorities to Spring Security objects (UserDetails and GrantedAuthorities)
        return new User(
                foundUser.getUsername(),
                foundUser.getPassword(),
                foundUser.isEnabled(),
                foundUser.isAccountNoExpired(),
                foundUser.isCredentialNoExpired(),
                foundUser.isAccountNoLocked(),
                getAuthoritiesFromRoles(foundUser.getRoles())
        );
	}

	private Set<SimpleGrantedAuthority> getAuthoritiesFromRoles(Set<RoleEntity> roles){

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



	public UserDetailServiceImpl(UserRepository userRepo){
		this.userRepo = userRepo;
	}
	private final UserRepository userRepo;
}