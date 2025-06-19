package com.cesar.JwtServer.presentation;

import com.cesar.JwtServer.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<?> login(@RequestBody LogInRequest loginRequest, HttpServletResponse response){

		//JWT token response if successful auth
		String token = authService.login(loginRequest);

		if(token != null){

			//Set token as Http only cookie
			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
//			cookie.setSecure(true);
			cookie.setMaxAge(60*60*24);

			response.addCookie(cookie);
			return ResponseEntity.status(HttpStatus.OK).body("Successfully authenticated!");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated");
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest, HttpServletResponse response){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(authService.signup(signupRequest));
	}


	public AuthController(AuthService authService){
		this.authService = authService;
	}
	private final AuthService authService;
}