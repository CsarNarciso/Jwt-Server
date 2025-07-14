package com.cesar.JwtServer.presentation.dto;

public record SignUpRequest(
	String username,
	String password
){}