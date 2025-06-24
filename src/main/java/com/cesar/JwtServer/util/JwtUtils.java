package com.cesar.JwtServer.util;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import com.cesar.JwtServer.persistence.entity.JwtTokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils{

	private final String ISSUER;
	private final Algorithm ACCESS_ALGORITHM;
	private final Algorithm REFRESH_ALGORITHM;
	private final int ACCESS_EXPIRES_IN;
	private final int REFRESH_EXPIRES_IN;

	public JwtUtils(@Value("${jwt.issuer}") String ISSUER,
					@Value("${jwt.access.secret}") String ACCESS_SECRET,
					@Value("${jwt.refresh.secret}") String REFRESH_SECRET,
					@Value("${jwt.access.expiresIn}") int ACCESS_EXPIRES_IN,
					@Value("${jwt.refresh.expiresIn}") int REFRESH_EXPIRES_IN){
		this.ISSUER = ISSUER;
		this.ACCESS_ALGORITHM = Algorithm.HMAC256(ACCESS_SECRET);
		this.REFRESH_ALGORITHM = Algorithm.HMAC256(REFRESH_SECRET);
		this.ACCESS_EXPIRES_IN = ACCESS_EXPIRES_IN;
		this.REFRESH_EXPIRES_IN = REFRESH_EXPIRES_IN;
	}

	public String createToken(Authentication authentication, JwtTokenType type){
		
		//Get current authenticated user username
		String username = authentication.getPrincipal().toString();
		
		//Get all authorities (permissions and roles)
		String authorities = authentication.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(","));
		
		//Generate JWT token
		int expiresIn;
		Algorithm algorithm;

		if(type == JwtTokenType.ACCESS) {
			expiresIn = ACCESS_EXPIRES_IN;
			algorithm = ACCESS_ALGORITHM;
		}
		else {
			expiresIn = REFRESH_EXPIRES_IN;
			algorithm = REFRESH_ALGORITHM;
		}

		return JWT.create()
			.withIssuer(ISSUER)
			.withSubject(username)
			.withClaim("authorities", authorities)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + expiresIn))
			.withNotBefore(new Date(System.currentTimeMillis()))
			.withJWTId(UUID.randomUUID().toString())
			.sign(algorithm);
	}

	public DecodedJWT validateToken(String token, JwtTokenType type){
		
		try{
			Algorithm algorithm = (type == JwtTokenType.ACCESS ? ACCESS_ALGORITHM : REFRESH_ALGORITHM);
			JWTVerifier verifier = JWT.require(algorithm)
							.withIssuer(ISSUER)
							.build();
							
			//Get decoded Jwt token if successful verification
			return verifier.verify(token);
			
		} catch(JWTVerificationException ex){
			throw new JWTVerificationException("Invalid token. No authenticated: " + ex.getMessage());
		}
	}
	
	public String extractUsername(DecodedJWT decodedToken){
		return decodedToken.getSubject();
	}
	
	public Claim getSpecificClaim(DecodedJWT decodedToken, String claimName){
		return decodedToken.getClaim(claimName);
	}
}