package com.cesar.JwtServer.service;

import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.RoleRepository;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;
import com.cesar.JwtServer.presentation.dto.SignUpResponse;
import com.cesar.JwtServer.util.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

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
                .roles(roles)
                .build();

        userRepo.save(user);

        return SignUpResponse
                .builder()
                .username(username)
                .created(true)
                .build();
    }

    private Authentication authenticateByUsernameAndPassword(String username, String password){

        UserDetails user = userDetailService.loadUserByUsername(username);

        //If user exists (correct credentials)
        if(user!=null && passwordEncoder.matches(password, user.getPassword())){

            //Authenticate user in Security context
            Authentication authentication = new
                    UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;
        }

        throw new BadCredentialsException("Invalid username or password");
    }



    public AuthService(UserDetailServiceImpl userDetailService, UserRepository userRepo, RoleRepository roleRepo, JwtUtils jwtUtils){
        this.userDetailService = userDetailService;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    private final UserDetailServiceImpl userDetailService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;
}