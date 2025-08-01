package com.cesar.JwtServer.presentation;

import com.cesar.JwtServer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cesar.JwtServer.presentation.dto.LogInRequest;
import com.cesar.JwtServer.presentation.dto.SignUpRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController{

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}


	@PostMapping("/register")
	public ResponseEntity<?> signup(@RequestBody SignUpRequest signupRequest) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(authService.signup(signupRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LogInRequest loginRequest, HttpServletResponse res){
		authService.login(loginRequest, res);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(Map.of("message","Successfully authenticated!"));
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(HttpServletRequest req, HttpServletResponse res) {
		authService.refresh(req, res);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(Map.of("message", "Tokens successfully refreshed!"));
	}
}