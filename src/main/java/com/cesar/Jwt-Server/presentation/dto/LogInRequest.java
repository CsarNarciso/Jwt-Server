package com.cesar.Jwt-Server.presentation.dto;

public record LogInRequest(
	String username,
	String password
){}