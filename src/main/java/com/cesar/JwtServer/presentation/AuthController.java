package com.cesar.JwtServer.presentation;

import com.cesar.JwtServer.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;

@RestController
@RequestMapping("/auth")
public class AuthController{
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LogInRequest loginRequest){
		//JWT token response if successful auth
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(authService.login(loginRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(authService.signup(signupRequest));
	}


	public AuthController(AuthService authService){
		this.authService = authService;
	}
	private final AuthService authService;
}