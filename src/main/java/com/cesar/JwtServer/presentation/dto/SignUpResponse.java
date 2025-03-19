package com.cesar.JwtServer.presentation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
	private String username;
	private boolean created;
}