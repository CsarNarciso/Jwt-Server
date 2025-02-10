package com.cesar.JwtServer.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;
import com.cesar.JwtServer.service.UserDetailServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController{
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LogInRequest loginRequest){
		//JWT token response if successful auth
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(userDetailsServiceImpl.login(loginRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(userDetailsServiceImpl.signup(signupRequest));
	}
	
	public AuthController(UserDetailServiceImpl userDetailsServiceImpl){
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}
	
	private final UserDetailServiceImpl userDetailsServiceImpl;
}