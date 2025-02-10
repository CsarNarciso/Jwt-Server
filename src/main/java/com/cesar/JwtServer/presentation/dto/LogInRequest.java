package com.cesar.JwtServer.presentation.dto;

public record LogInRequest(
	String username,
	String password
){}