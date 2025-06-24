package com.cesar.JwtServer.service;

import com.cesar.JwtServer.persistence.entity.JwtTokenType;
import com.cesar.JwtServer.persistence.entity.RoleEntity;
import com.cesar.JwtServer.persistence.entity.UserEntity;
import com.cesar.JwtServer.persistence.repository.UserRepository;
import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;
import com.cesar.JwtServer.presentation.dto.SignUpResponse;
import com.cesar.JwtServer.util.AuthorityUtils;
import com.cesar.JwtServer.util.JwtUtils;
import com.cesar.JwtServer.util.UserUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class AuthService {

    public String login(LogInRequest loginRequest){

        String username = loginRequest.username();
        String password = loginRequest.password();

        //Generate access token if successful auth
        return jwtUtils.createToken(authenticateByUsernameAndPassword(username, password), JwtTokenType.ACCESS);
    }

    public SignUpResponse signup(SignUpRequest signupRequest){

        //Get user data
        String username = signupRequest.username();
        String password = signupRequest.password();

        //Create and save new user in DB
        RoleEntity userRole = authorityUtils.getUserRole();
        UserEntity user = userUtils.buildUser(username, password, Set.of(userRole));
        user = userRepo.save(user);

        return SignUpResponse
                .builder()
                .username(user.getUsername())
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



    public AuthService(UserDetailServiceImpl userDetailService, UserRepository userRepo, UserUtils userUtils, JwtUtils jwtUtils, AuthorityUtils authorityUtils){
        this.userDetailService = userDetailService;
        this.userRepo = userRepo;
        this.userUtils = userUtils;
        this.jwtUtils = jwtUtils;
        this.authorityUtils = authorityUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    private final UserDetailServiceImpl userDetailService;
    private final UserRepository userRepo;
    private final UserUtils userUtils;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityUtils authorityUtils;
}