package com.cesar.Jwt-Server.presentation;

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
	
	public AuthController(UserDetailServiceImpl userDetailsServiceImpl){
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}
	
	private final UserDetailServiceImpl userDetailsServiceImpl;
}