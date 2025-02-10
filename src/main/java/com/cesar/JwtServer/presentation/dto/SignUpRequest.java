package com.cesar.JwtServer.presentation.dto;

import java.util.List;

public record SignUpRequest(
	String username,
	String password,
	List<String> roleNames
){}