package com.cesar.Jwt-Server.presentation.dto;

public record SignUpRequest(
	String username,
	String password,
	List<String> roleNames
){}