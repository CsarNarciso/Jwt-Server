package com.cesar.JwtServer.presentation.dto;

import lombok.Builder;

@Builder
public class SignUpResponse {
	private String username;
	private boolean created;
}